package com.luangeng.oauthserver.controller;

import com.luangeng.oauthserver.dao.UserRepository;
import com.luangeng.oauthserver.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String list(Model model) throws Exception {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(User user) throws Exception {
        user = userRepository.save(user);
        return "user";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String del(long id) throws Exception {
        userRepository.deleteById(id);
        return "user";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(User user) throws Exception {
        userRepository.save(user);
        return "user";
    }

}
