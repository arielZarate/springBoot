package com.arielzarate.Spring_thymeleaf_Mvc.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api")
public class apiController {

    @GetMapping
    public String api(Model model) {
        log.info("Entrando a la http://localhost:8080/api");
        model.addAttribute("api", "Estas es la api con thymeleft y java springboot \uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB ");
        return "api";
    }


}


