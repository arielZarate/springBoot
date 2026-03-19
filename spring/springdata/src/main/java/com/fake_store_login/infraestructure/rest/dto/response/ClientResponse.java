package com.fake_store_login.infraestructure.rest.dto.response;

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
    private Boolean active;

    //address fields
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
