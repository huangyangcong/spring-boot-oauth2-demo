package com.luangeng.oauthserver.ding;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 500L;
    private Object principal;
    private Object credentials;

    public MyToken(Object principal, Object credentials) {
        super((Collection) null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public MyToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
