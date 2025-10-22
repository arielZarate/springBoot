package com.arielzarate.error.model;

import lombok.Getter;

@Getter
public class ApplicationError {
    /**
     * -- GETTER --
     * Getters
     */
    private final String message;
    private final int status;


    /**
     * Internal Server Error con sobrecarga
     */
    public static ApplicationError serverError(String detail) {
        return new ApplicationError(
                "Internal Server Error : " + detail,
                500
        );
    }

    public static ApplicationError serverError() {
        return new ApplicationError("Internal Server Error", 500);
    }

    public static ApplicationError badRequest(String detail) {
        return new ApplicationError("Bad Request " + detail, 400);
    }

    public static ApplicationError conflict(String detail) {
        return new ApplicationError("Conflict: " + detail, 409);
    }

    /**
     * Solo retorno el message y el code
     */
    public static ApplicationError notFound(String field) {
        return new ApplicationError("The field  " + field + " not found", 404);
    }


    /**
     * Los dos constructores son necesarios para poder crear errores con o sin origen
     */
    public ApplicationError(String message, int status) {
        this.message = message;
        this.status = status;

    }


}
