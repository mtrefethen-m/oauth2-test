package com;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/hello")
    public String greetings() {
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

}
