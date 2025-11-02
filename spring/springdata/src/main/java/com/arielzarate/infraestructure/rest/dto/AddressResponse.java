package com.arielzarate.infraestructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressResponse {

    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
