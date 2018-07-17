package com.luangeng.oauthclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class OauthClientConfig {

    @Value("${accessTokenUri}")
    private String accessTokenUri;

    @Value("${userAuthorizationUri}")
    private String userAuthorizationUri;

    @Bean
    public OAuth2RestTemplate restTemplate(OAuth2ClientContext clientContext) {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setId("server/client");
        details.setClientId("photo");
        details.setClientSecret("photo");
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(Arrays.asList("read"));
        return new OAuth2RestTemplate(details, clientContext);
    }

}
