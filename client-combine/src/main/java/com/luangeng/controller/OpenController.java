package com.luangeng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/open")
public class OpenController {

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("msg", "This is open msg");
        return "index";
    }


}
