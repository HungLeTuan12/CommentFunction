package com.ecommerce.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// throw in App exception
public enum ErrorCode {
    UNAUTHENTICATED("Unauthenticated !!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission !!", HttpStatus.FORBIDDEN);
    private String message;
    private HttpStatusCode httpStatusCode;
}
