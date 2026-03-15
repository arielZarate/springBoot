package com.arielzarate.infraestructure.rest.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    //address fields
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

}
