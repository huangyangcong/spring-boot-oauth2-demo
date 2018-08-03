package com.luangeng.oauthserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/auth").setViewName("auth");
    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //映射url中的public/*到classpath下的public文件夹
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }
//
//处理web项目的静态文件
@Override
public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
}

}
