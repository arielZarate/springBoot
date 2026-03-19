package com.fake_store_login.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}



/**
 * como solo consumo la api de login, basta con crear un DTO para el request,
 * pero lo hago para mantener la consistencia y claridad en el código.
 * */