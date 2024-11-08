package com.ecommerce.comment.response;

import com.ecommerce.comment.constant.Status;
// This class extends from ApiResponse
// And it will handle all of error response from controller
public class ErrorResponse<T> extends ApiResponse<T>{
    public ErrorResponse(String message) {
        super(Status.FAILED,message,null);
    }

    public ErrorResponse(String message,T data){
        super(Status.FAILED,message,data);
    }
}
