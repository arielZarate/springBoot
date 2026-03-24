package com.arielzarate.Holamundo_Thymeleaf_Mvc.controller;


import com.arielzarate.Holamundo_Thymeleaf_Mvc.domain.Individuo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
public class IndividuoController {



    @GetMapping("individuo")
    public String individuo(Model model){
        log.info("get - individuo ");

        //======================================
        Individuo individuo = new Individuo();

        individuo.setNombre("Ariel");
        individuo.setApellido("zarate");
        individuo.setCorreo("ariel@gmail.com");
        individuo.setEdad("38");
        individuo.setTelefono("351456789");

        //======================================

        //agrego el individuo al modelo
        model.addAttribute("individuo",individuo);

        return "individuo";

    }

}
