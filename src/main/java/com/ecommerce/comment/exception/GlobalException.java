package com.ecommerce.comment.exception;

import com.ecommerce.comment.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// Spring know exception happen
public class GlobalException {
    // Catch all method related runtime exception
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> handlingRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    // Catch all field related validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = !ex.getBindingResult().getAllErrors().isEmpty() ?
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage() : "Unknown error, please check input";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(errorMessage));
    }
    // Catch all method return not found
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse<>(ex.getMessage()));
    }
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ErrorResponse<String>> handleAppException(AppException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(ex.getMessage()));
    }
}
