package com.arielzarate.error.model.exception;


import com.arielzarate.error.model.ApplicationError;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationError error;

    public ApplicationException(ApplicationError error) {
        super(error.getMessage());
        this.error = error;
    }

}
