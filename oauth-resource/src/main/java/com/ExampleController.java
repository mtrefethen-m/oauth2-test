package com;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/hello")
    public String greetings(@OAuthPrincipal OAuthUser oAuthUser) {
        return "Hello";
    }

    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                "http://localhost:8081/oauth/check_token");
        tokenService.setClientId("clientIdPassword");
        tokenService.setClientSecret("secret");
        return tokenService;
    }

    @PreAuthorize("#oauth2.hasScope('order.read')")
    @GetMapping("/another")
    public String another(@AuthenticationPrincipal String userName) {
        return userName;
    }

}
