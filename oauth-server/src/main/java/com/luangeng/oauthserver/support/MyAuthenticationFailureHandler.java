package com.luangeng.oauthserver.support;

import com.luangeng.oauthserver.exception.DingServiceException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private String url;

    public MyAuthenticationFailureHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof LockedException) {
            redirectStrategy.sendRedirect(request, response, this.url + "2");
        } else if (exception instanceof DisabledException) {
            redirectStrategy.sendRedirect(request, response, this.url + "2");
        } else if (exception instanceof AccountExpiredException) {
            redirectStrategy.sendRedirect(request, response, this.url + "2");
        } else if (exception instanceof DingServiceException) {
            redirectStrategy.sendRedirect(request, response, this.url + "3");
        } else if (exception instanceof CredentialsExpiredException) {
            redirectStrategy.sendRedirect(request, response, this.url + "2");
        } else if (exception instanceof BadCredentialsException) {
            redirectStrategy.sendRedirect(request, response, this.url + "1");
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

}
