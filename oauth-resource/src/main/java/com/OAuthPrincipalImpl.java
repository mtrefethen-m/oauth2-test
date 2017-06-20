package com;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;

public class OAuthPrincipalImpl implements HandlerMethodArgumentResolver {

    private ExpressionParser parser = new SpelExpressionParser();

    public boolean supportsParameter(MethodParameter parameter) {
        return findMethodAnnotation(OAuthPrincipal.class, parameter) != null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#
     * resolveArgument (org.springframework.core.MethodParameter,
     * org.springframework.web.method.support.ModelAndViewContainer,
     * org.springframework.web.context.request.NativeWebRequest,
     * org.springframework.web.bind.support.WebDataBinderFactory)
     */
    public OAuthUser resolveArgument(MethodParameter parameter,
                                     ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                     WebDataBinderFactory binderFactory) throws Exception {
        OAuth2Authentication authentication;

        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication) {
            authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        } else {
            throw new IllegalStateException("WTF");
        }

        OAuthUser user = new OAuthUser(authentication.getPrincipal().toString(), authentication.getOAuth2Request().getScope(), authentication.getAuthorities());

        OAuthPrincipal authPrincipal = findMethodAnnotation(OAuthPrincipal.class, parameter);

        if (user != null
                && !parameter.getParameterType().isAssignableFrom(user.getClass())) {

            if (authPrincipal.errorOnInvalidType()) {
                throw new ClassCastException(user + " is not assignable to "
                        + parameter.getParameterType());
            } else {
                return null;
            }
        }
        return user;
    }

    /**
     * Obtains the specified {@link Annotation} on the specified {@link MethodParameter}.
     *
     * @param annotationClass the class of the {@link Annotation} to find on the
     *                        {@link MethodParameter}
     * @param parameter       the {@link MethodParameter} to search for an {@link Annotation}
     * @return the {@link Annotation} that was found or null.
     */
    private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
                                                          MethodParameter parameter) {
        T annotation = parameter.getParameterAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
        for (Annotation toSearch : annotationsToSearch) {
            annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(),
                    annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}
