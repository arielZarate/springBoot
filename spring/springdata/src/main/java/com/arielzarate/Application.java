package com.arielzarate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Este codigo es un ejemplo de una aplicación Java que utiliza Spring Data JPA para gestionar entidades de tipo "Persona" en una base de datos.
 * Se usara arquitectura hexagonal y se implementara un CRUD completo.
 * El código incluye la configuración de la conexión a la base de datos, la definición de la entidad "Cliente" ,"Productos", el repositorio para realizar operaciones CRUD y un servicio para manejar la lógica de negocio.
 * El código también incluye un controlador REST para exponer una API que permite crear, leer, actualizar y eliminar personas.
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}