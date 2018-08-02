package com.luangeng.oauthserver.ding;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ThirdLoginFilter extends AbstractAuthenticationProcessingFilter {

    public ThirdLoginFilter() {
        super(new AntPathRequestMatcher("/auth", "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String code = httpServletRequest.getParameter("code");
        String type = httpServletRequest.getParameter("type");

        MyToken myToken = new MyToken(code, type);
        this.setDetails(httpServletRequest, myToken);

        return this.getAuthenticationManager().authenticate(myToken);
    }

    protected void setDetails(HttpServletRequest request, MyToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
