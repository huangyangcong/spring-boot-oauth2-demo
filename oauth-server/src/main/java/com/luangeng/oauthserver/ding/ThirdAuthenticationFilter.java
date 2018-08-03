package com.luangeng.oauthserver.ding;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第三方登录授权过滤器
 */
public class ThirdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public ThirdAuthenticationFilter() {
        super(new AntPathRequestMatcher("/auth", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String code = request.getParameter("login_code");
        String type = request.getParameter("login_type");
        AbstractAuthenticationToken authToken = new ThirdAuthenticationToken(code, "");
        ((ThirdAuthenticationToken) authToken).setLoginType(type);
        setDetails(request, authToken);
        Authentication auth = this.getAuthenticationManager().authenticate(authToken);
        return auth;
    }

    protected void setDetails(HttpServletRequest request,
                              AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
