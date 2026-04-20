package com.arielzarate.Spring_thymeleaf_Mvc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    
    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String address;
    private String email;
    private String cuit;

}