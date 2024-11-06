package com.ecommerce.comment.response;

public class ErrorResponse<T> extends ApiResponse<T>{
    public ErrorResponse(String message) {
        super("failed",message,null);
    }

    public ErrorResponse(String message,T data){
        super("failed",message,data);
    }
}
