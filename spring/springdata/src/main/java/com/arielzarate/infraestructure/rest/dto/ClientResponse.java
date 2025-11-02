package com.arielzarate.infraestructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClientResponse {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressResponse address;
}
