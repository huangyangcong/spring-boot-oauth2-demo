package com.luangeng.oauthserver.controller;

import com.luangeng.oauthserver.dao.ClientRepository;
import com.luangeng.oauthserver.vo.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Client
 */
@Controller
@RequestMapping("/client")
@PreAuthorize("hasRole('ADMIN')")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping
    public String list(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "client";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(Client client) {
        client = clientRepository.save(client);
        return "client";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String del(String id) {
        clientRepository.deleteById(id);
        return "client";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String mod(Client client) {
        client = clientRepository.save(client);
        return "client";
    }


}
