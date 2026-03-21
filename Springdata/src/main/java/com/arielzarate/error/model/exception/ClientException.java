package com.arielzarate.error.model.exception;

import com.arielzarate.error.model.ClientError;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ClientError error;

    public ClientException(ClientError clientError) {
        super(clientError.getMessage());
        this.error = clientError;
    }

}
