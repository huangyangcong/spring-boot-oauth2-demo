package com.luangeng.oauthserver.ding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 第三方登录验证器
 */
@Component
public class ThirdAuthenticationProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    protected MessageSourceAccessor messages;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private DingdingService dingService;

    private UserDetailsChecker authenticationChecks;

    private GrantedAuthoritiesMapper authoritiesMapper;

    public ThirdAuthenticationProvider() {
        messages = SpringSecurityMessageSource.getAccessor();
        authenticationChecks = new DefaultAuthenticationChecks();
        authoritiesMapper = new NullAuthoritiesMapper();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        String type = ((ThirdAuthenticationToken) authentication).getLoginType();
        if (!type.equalsIgnoreCase("dingding")) {
            throw new UsernameNotFoundException("登录类型错误：" + type);
        }
        String userId = dingService.getEmployeeNum(code);
        if (userId == null) {
            throw new UsernameNotFoundException("钉钉扫码登录失败，未获取到用户信息");
        }

        UserDetails user = userService.loadUserByUsername("user");
        if (user == null) {
            throw new UsernameNotFoundException("用户在系统中不存在：" + userId);
        }
        authenticationChecks.check(user);

        ThirdAuthenticationToken result = new ThirdAuthenticationToken(user, "", authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (ThirdAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public void setUserService(UserDetailsService userService) {
        this.userService = userService;
    }

    private class DefaultAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");
                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"));
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");
                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");
                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"));
            }
            if (!user.isCredentialsNonExpired()) {
                logger.debug("User account credentials have expired");
                throw new CredentialsExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                        "User credentials have expired"));
            }
        }
    }

}
