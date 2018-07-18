package com.luangeng.oauthserver.support;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken daccessToken = (DefaultOAuth2AccessToken)accessToken;
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("msg", "empty");
            daccessToken.setAdditionalInformation(additionalInfo);
            return daccessToken;
        }
        return accessToken;
    }

}