package com.luangeng.oauthserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        //new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
//                .inMemoryAuthentication().withUser("z").password("z").roles("USER").
//                and().withUser("admin").password("admin").roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/webjars/**", "/public/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()

                .exceptionHandling()
                .accessDeniedPage("/login?authorization_error=true")
                .and()

                //创建session的策略
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // .and()

                .headers().frameOptions().disable()
                .and()

                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()

                //.cors().configurationSource(configurationSource()).and()

                .logout().addLogoutHandler(logoutHandler())
                .and()

                .formLogin()
                .loginProcessingUrl("/login")
                .failureUrl("/login?authorization_error=true")
                .loginPage("/login");
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            try {
                String redirect = httpServletRequest.getParameter("redirect_uri");
                if (redirect != null) {
                    httpServletResponse.sendRedirect(redirect);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
