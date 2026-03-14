package com.arielzarate.infraestructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ClientResponse {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressResponse address;
}
