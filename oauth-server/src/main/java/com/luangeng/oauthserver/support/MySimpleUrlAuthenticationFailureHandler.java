package com.luangeng.oauthserver.support;

import com.luangeng.oauthserver.exception.DingServiceException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private String url;

    public MySimpleUrlAuthenticationFailureHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof LockedException) {
            handleUserLockedAndExpiredException(request, response, "locked");
            return;
        } else if (exception instanceof DisabledException) {
            handleUserLockedAndExpiredException(request, response, "disabled");
            return;
        } else if (exception instanceof AccountExpiredException) {
            handleUserLockedAndExpiredException(request, response, "expired");
            return;
        } else if (exception instanceof DingServiceException) {
            handleUserLockedAndExpiredException(request, response, "dingError");
            return;
        } else if (exception instanceof CredentialsExpiredException) {
            handleUserLockedAndExpiredException(request, response, "expired");
            return;
        }
        super.onAuthenticationFailure(request, response, exception);
    }

    private void handleUserLockedAndExpiredException(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        String redirect_url = this.url + msg;
        redirectStrategy.sendRedirect(request, response, redirect_url);
    }

}
