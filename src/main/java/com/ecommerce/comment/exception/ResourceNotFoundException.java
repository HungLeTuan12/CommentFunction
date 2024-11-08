package com.ecommerce.comment.exception;
// This class will check when have method, entity is not exist
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
