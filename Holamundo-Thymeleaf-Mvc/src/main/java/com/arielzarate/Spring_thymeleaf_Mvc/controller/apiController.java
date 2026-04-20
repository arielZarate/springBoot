package com.arielzarate.Holamundo_Thymeleaf_Mvc.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class apiController {

    @GetMapping("/")
    public String api(Model model) {
        log.info("Entrando a la  api/");
        model.addAttribute("api", "Estas es la api con thymeleft y java springboot :)");
        return "api";
    }


}


