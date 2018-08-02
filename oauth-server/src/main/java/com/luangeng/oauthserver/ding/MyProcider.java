package com.luangeng.oauthserver.ding;

import com.luangeng.oauthserver.service.DingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class MyProcider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DingService dingService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        authentication.getDetails();
        authentication.getCredentials();

        DingService.UserInfo userinfo = dingService.getuserInfo(code);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userinfo.getNick());

        MyToken result = new MyToken(userinfo.getNick(), authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return MyToken.class.isAssignableFrom(aClass);
    }
}
