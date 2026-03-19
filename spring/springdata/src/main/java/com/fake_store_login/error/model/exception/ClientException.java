package com.fake_store_login.error.model.exception;

import com.fake_store_login.error.model.ClientError;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ClientError error;

    public ClientException(ClientError clientError) {
        super(clientError.getMessage());
        this.error = clientError;
    }

}
