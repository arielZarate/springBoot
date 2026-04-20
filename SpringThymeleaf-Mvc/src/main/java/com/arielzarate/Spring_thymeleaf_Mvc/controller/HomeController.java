package com.arielzarate.Spring_thymeleaf_Mvc.controller;

import com.arielzarate.Spring_thymeleaf_Mvc.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    private final ClientService clientService;

    public HomeController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("home page start");
        model.addAttribute("clients", clientService.findAll());
        return "index";
    }

}