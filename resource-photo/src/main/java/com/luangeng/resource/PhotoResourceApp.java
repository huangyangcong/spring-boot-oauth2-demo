package com.luangeng.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class PhotoResourceApp extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(PhotoResourceApp.class, args);
    }

}
