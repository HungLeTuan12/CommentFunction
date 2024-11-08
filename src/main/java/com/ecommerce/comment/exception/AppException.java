package com.ecommerce.comment.exception;
// This class catch customize exception
public class AppException extends RuntimeException{
    // Extends method from runtime exception
    public AppException(String message) {
        super(message);
    }
}
