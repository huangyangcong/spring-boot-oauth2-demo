package com.luangeng.oauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OauthServerApp {

    public static void main(String[] args) {
        SpringApplication.run(OauthServerApp.class);
    }

}
