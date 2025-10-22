package com.arielzarate;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * esta @Configuration le dice a spring que debe guardar esta clase en su contexto
 * y que tiene metodos que  devolvera un Bean
 */

@Configuration
public class AppConfig {


    /**
     * Este metodo devuelve una instancia que queremos que spring guarde en su contexto
     */
    @Bean
    public Version version() {
        return new Version(1, 0, 0);
    }

    public void otroMetodo() {
        System.out.println("Este metodo no se guardara en el contexto");
    }

}
