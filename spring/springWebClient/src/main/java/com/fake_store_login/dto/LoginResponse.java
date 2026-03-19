package com.fake_store_login.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
}


/**
 * La api de login de FakeStoreAPI devuelve un JSON con un campo "token" que contiene el token de autenticación.
 * Por eso el DTO LoginResponse solo tiene un campo "token".
 * */