package com.luangeng.oauthserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

//
//@CrossOrigin("*")
// 如果在CorsFilter中配置有"Access-Control-Allow-Origin", 则controller中方法注解@CrossOrigin失效。（即filter的优先级高于@CrossOrigin方法注解）
@Controller
public class MeController {

    @ResponseBody
    @RequestMapping("/me")
    public Principal me(Principal principal) {
        return principal;
    }

    @ResponseBody
    @RequestMapping("/msg")
    public String msg() {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "this msg is from oauth server..";
    }
}
