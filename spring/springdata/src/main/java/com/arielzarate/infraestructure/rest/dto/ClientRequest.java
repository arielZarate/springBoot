package com.arielzarate.infraestructure.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressRequest address; //puede ser null
}
