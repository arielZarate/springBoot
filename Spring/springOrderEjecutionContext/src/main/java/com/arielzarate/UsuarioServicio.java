package com.fake_store_login;

import org.springframework.stereotype.Service;

@Service

public class UsuarioServicio {

    private final Version version;

    public UsuarioServicio(Version version) {
        this.version = version;
    }

}
