package com.luangeng.oauthserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 配置/me在resource里面，使resource处理该请求，无权限返回401，不配置的话由springsecret处理，返回登录页
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String AUTH_RESOURCE_ID = "server";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(AUTH_RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/me", "/msg")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

}
