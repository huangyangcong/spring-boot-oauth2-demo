package com.luangeng.oauthserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User
 */
@Controller
@RequestMapping("/client")
@PreAuthorize("hasRole('ADMIN')")
public class ClientController {

    @RequestMapping
    public String list(Model model) throws Exception {
        return "client";
    }

}
