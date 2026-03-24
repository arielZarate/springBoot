package com.arielzarate.Holamundo_Thymeleaf_Mvc.controller;


import com.arielzarate.Holamundo_Thymeleaf_Mvc.domain.Individuo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RequestMapping("/individuos")
@Controller
public class IndividuoController {



    @GetMapping("/")
    public String individuo(Model model){
        log.info("request get-individuos ");

        //======================================
        Individuo individuo = new Individuo();

        individuo.setNombre("Ariel");
        individuo.setApellido("zarate");
        individuo.setCorreo("ariel@gmail.com");
        individuo.setEdad("38");
        individuo.setTelefono("351456789");


        Individuo individuo2 = new Individuo();
        individuo2.setNombre("Juan");
        individuo2.setApellido("Pérez");
        individuo2.setCorreo("juan@gmail.com");
        individuo2.setEdad("25");
        individuo2.setTelefono("351123456");

        Individuo individuo3 = new Individuo();
        individuo3.setNombre("María");
        individuo3.setApellido("Gómez");
        individuo3.setCorreo("maria@gmail.com");
        individuo3.setEdad("30");
        individuo3.setTelefono("351987654");


        //======================================

        //agrego el individuo al modelo
        model.addAttribute("individuo",individuo);


        //1er, 2do y 3er individuo -> lista

        List<Individuo> list = Arrays.asList(individuo, individuo2, individuo3);
      //  List<Individuo> list = new ArrayList<>();

        model.addAttribute("individuos",list);

        log.info("response get-individuos");
        return "individuo";

    }

}
