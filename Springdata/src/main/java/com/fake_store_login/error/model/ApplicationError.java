package com.fake_store_login.error.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationError {
    /**
     * -- GETTER --
     * Getters
     */
    private final String message;
    private final HttpStatus status;


    /**
     * Internal Server Error con sobrecarga
     */
    public static ApplicationError serverError(String detail) {
        return new ApplicationError(
                "Internal Server Error : " + detail,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static ApplicationError serverError() {
        return new ApplicationError("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ApplicationError badRequest(String detail) {
        return new ApplicationError("Bad Request " + detail, HttpStatus.BAD_REQUEST);
    }

    public static ApplicationError conflict(String detail) {
        return new ApplicationError("Conflict: " + detail, HttpStatus.CONFLICT);
    }

    /**
     * Solo retorno el message y el code
     */
    public static ApplicationError notFound(String field) {
        return new ApplicationError("The field  " + field + " not found", HttpStatus.NOT_FOUND);
    }


    /**
     * Los dos constructores son necesarios para poder crear errores con o sin origen
     */
    public ApplicationError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;

    }


}
