package com.ecommerce.comment.response;

import com.ecommerce.comment.constant.Status;
// This class extends from ApiResponse
// And it will handle all of success response from controller
public class SuccessResponse<T> extends ApiResponse<T> {
    public SuccessResponse(String message) {
        super(Status.SUCCESS,message,null);
    }

    public SuccessResponse(String message,T data){
        super(Status.SUCCESS,message,data);
    }
}
