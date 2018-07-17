package com.luangeng.ssoclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class SsoClientApp2 extends WebSecurityConfigurerAdapter {

    @Value("${oauthserver}/logout")
    private String serverLogout;

    @Value("${oauthserver}")
    private String serverPath;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SsoClientApp2.class, args);
    }

    @Bean
    OAuth2RestTemplate restTemplate(OAuth2ClientContext clientContext, OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, clientContext);
    }

    @RequestMapping("/user")
    public String user(Principal principal) {
        Authentication p2 = SecurityContextHolder.getContext().getAuthentication();
        //return ((OAuth2AuthenticationDetails) p2.getDetails()).getTokenValue();
        return p2.getName();
    }

    @RequestMapping("/user2")
    @ResponseBody
    public String user2() {
        return restTemplate.getForObject(serverPath + "/msg", String.class);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout().logoutSuccessUrl(serverLogout).permitAll()
                .and().antMatcher("/**").authorizeRequests().antMatchers("/login**", "/logout", "/webjars/**", "/error**").permitAll().anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().disable();

    }

}
