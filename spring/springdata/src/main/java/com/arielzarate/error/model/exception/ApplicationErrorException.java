package com.arielzarate.error.model.exception;


import com.arielzarate.error.model.ApplicationError;
import lombok.Getter;

@Getter
public class ApplicationErrorException extends RuntimeException {
    private final ApplicationError error;

    public ApplicationErrorException(ApplicationError error) {
        super(error.getMessage());
        this.error = error;
    }

}
