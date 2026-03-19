package com.fake_store_login.error.model.exception;


import com.fake_store_login.error.model.ApplicationError;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationError error;

    public ApplicationException(ApplicationError error) {
        super(error.getMessage());
        this.error = error;
    }

}
