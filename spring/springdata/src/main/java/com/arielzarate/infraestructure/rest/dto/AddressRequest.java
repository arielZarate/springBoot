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
public class AddressRequest {

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;


}
