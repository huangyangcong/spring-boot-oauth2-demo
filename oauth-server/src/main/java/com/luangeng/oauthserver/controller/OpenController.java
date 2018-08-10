package com.luangeng.oauthserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open")
public class OpenController {

    @RequestMapping()
    public String getTokens() {
        return "this is a open msg..";
    }

}
