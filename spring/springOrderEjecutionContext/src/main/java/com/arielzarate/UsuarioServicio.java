package com.arielzarate;

import org.springframework.stereotype.Service;

@Service

public class UsuarioServicio {

    private final Version version;

    public UsuarioServicio(Version version) {
        this.version = version;
    }

}
