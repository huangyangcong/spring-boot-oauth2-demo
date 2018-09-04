package com.luangeng.oauthserver.controller;

import com.luangeng.oauthserver.dao.RoleRepository;
import com.luangeng.oauthserver.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Role
 */
@Controller
@RequestMapping("/role")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping
    public String list(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "role";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(Role role) {
        if (!role.getName().isEmpty()) {
            role = roleRepository.save(role);
        }
        return "redirect:/role";
    }

    @RequestMapping("/d/{id}")
    public String del(@PathVariable("id") int id) {
        roleRepository.deleteById(id);
        return "redirect:/role";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String mod(Role role) {
        roleRepository.save(role);
        return "role";
    }

}
