package com.ishop.sbinternetshop.exceptions;

public class APIException extends RuntimeException {
    // custom exception class for all problems while returning API responses (except resource not found)
    private static final long serialVersionUID = 1L;

    public APIException() {
    }
    public APIException(String message) {
        super(message);
    }
}
