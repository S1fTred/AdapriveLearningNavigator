package com.example.adaprivelearningnavigator.service.exception;

public class AiRouteGenerationException extends RuntimeException {
    public AiRouteGenerationException(String message) {
        super(message);
    }

    public AiRouteGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
