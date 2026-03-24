package com.arielzarate.Holamundo_Thymeleaf_Mvc.domain;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Individuo {

    private String nombre, apellido, telefono, correo, edad;
}
