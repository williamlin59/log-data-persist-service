package com.example.logdatapersistservice.exception;

public class LogDataPersistServiceException extends RuntimeException {
    public LogDataPersistServiceException(String message) {
        super(message);
    }

    public LogDataPersistServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
