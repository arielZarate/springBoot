package com.arielzarate.error.model;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientError {
    private final String message;
    private final HttpStatus status;

    public ClientError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }


    public static ClientError clientNotFound(String field) {
        return new ClientError("The client with the field " + field + " not found", HttpStatus.NOT_FOUND);
    }

    public static ClientError clientConflict(String detail) {
        return new ClientError("Conflict: " + detail, HttpStatus.CONFLICT);
    }

    public static ClientError clientBadRequest(String detail) {
        return new ClientError("Bad Request " + detail, HttpStatus.BAD_REQUEST);
    }

    public static ClientError serverError(String detail) {
        return new ClientError("Internal Server Error : " + detail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ClientError serverError() {
        return new ClientError("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
