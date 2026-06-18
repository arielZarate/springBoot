package com.arielzarate.Spring_thymeleaf_Mvc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    
    private Long id;
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Phone is required")
    private String phone;
    @NotEmpty(message = "Address is required")
    private String address;
    @NotEmpty(message = "Email is required")
    @Email(message = "Formato de email inválido")
    private String email;
    @NotEmpty(message = "Cuit is required")
    private String cuit;

}