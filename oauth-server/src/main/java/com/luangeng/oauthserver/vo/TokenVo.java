package com.luangeng.oauthserver.vo;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

public class TokenVo {

    private DefaultOAuth2AccessToken accessToken;

    private String clientId;

    private String userName;

    private String userRole;

    public DefaultOAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(DefaultOAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
