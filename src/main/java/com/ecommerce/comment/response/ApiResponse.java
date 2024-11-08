package com.ecommerce.comment.response;

import com.ecommerce.comment.constant.Status;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Api response used for creating criteria class response for test API
public class ApiResponse<T> {
    @Enumerated
    private Status status;
    private String message;
    private T result;
}
