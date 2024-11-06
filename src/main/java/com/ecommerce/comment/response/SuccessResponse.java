package com.ecommerce.comment.response;

public class SuccessResponse<T> extends ApiResponse<T> {
    public SuccessResponse(String message) {
        super("success",message,null);
    }

    public SuccessResponse(String message,T data){
        super("success",message,data);
    }
}
