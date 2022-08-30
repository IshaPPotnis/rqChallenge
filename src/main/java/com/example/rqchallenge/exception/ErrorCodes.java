package com.example.rqchallenge.exception;

public enum ErrorCodes {

    TOO_MANY_READ_REQUESTS(429001),
    INTERNAL_SERVER_ERROR(500),
    NO_DATA_FOUND(404001),
    INVALID_DATA_FOUND(404002);

    private final int errorCode;

    ErrorCodes(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
