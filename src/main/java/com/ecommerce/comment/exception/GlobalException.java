package com.ecommerce.comment.exception;

import com.ecommerce.comment.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
// Spring know exception happen and catch it
// All of exception will handle in here
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
    // Custom exception
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ErrorResponse<String>> handleAppException(AppException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(ex.getMessage()));
    }
    // Handle exception when user do not have permission
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(new ErrorResponse<>(errorCode.getMessage()));
    }
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ErrorResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse<>(ex.getMessage()));
    }
}
