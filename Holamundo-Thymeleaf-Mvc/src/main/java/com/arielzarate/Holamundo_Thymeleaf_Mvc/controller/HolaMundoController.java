package com.arielzarate.Holamundo_Thymeleaf_Mvc.controller;


import com.arielzarate.Holamundo_Thymeleaf_Mvc.domain.Individuo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HolaMundoController {

    @GetMapping("/saludo")
    public String saludoMundo(Model model) {
        log.info("Entrando al endpoint /saludo");


        model.addAttribute("mensaje", "Hola Mundo desde Thymeleaf! con Model");
        model.addAttribute("parrafo", "Esto es un ejemplo de parrafo con thymelaft y Model");



        return "saludo";
    }


}


