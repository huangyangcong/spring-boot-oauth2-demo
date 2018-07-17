package com.luangeng.oauthserver.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义CorsConfigurationSource的CorsFilter，不使用DelegatingWebMvcConfiguration中mvcHandlerMappingIntrospector返回的默认配置
 */
@Configuration
public class CorsFilterConfiguration {

    private static final String[] ALLOWED_HEADERS = {"x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest", "Authorization"};

    @Bean
    public FilterRegistrationBean corsFilter() {
        List<String> allowedHeaders = Arrays.asList(ALLOWED_HEADERS);
        List<String> exposedHeaders = Arrays.asList(ALLOWED_HEADERS);
        List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
        List<String> allowedOrigins = Arrays.asList("*");

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(allowedHeaders);
        corsConfig.setAllowedMethods(allowedMethods);
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setExposedHeaders(exposedHeaders);
        corsConfig.setMaxAge(60 * 60L);
        corsConfig.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        //放在所有filter最前面
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
