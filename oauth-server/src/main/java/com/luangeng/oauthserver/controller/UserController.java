package com.luangeng.oauthserver.controller;

import com.luangeng.oauthserver.dao.UserRepository;
import com.luangeng.oauthserver.vo.User;
import com.luangeng.oauthserver.watchmen.Watchmen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * User
 */
@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping
    public String list(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user";
    }

    @Watchmen(time = 500)
    @RequestMapping(method = RequestMethod.POST)
    public String add(User user) {
        user = userRepository.save(user);
        return "redirect:/user";
    }

    @RequestMapping("/d/{id}")
    public String del(@PathVariable("id") long id) {
        userRepository.deleteById(id);
        return "redirect:/user";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String mod(User user) {
        userRepository.save(user);
        return "user";
    }

}
