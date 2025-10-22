package com.arielzarate.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
