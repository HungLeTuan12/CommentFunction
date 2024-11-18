package com.ecommerce.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Response when user login in system
 * If user login successfully, token will active and authenticated = true
 */
public class AuthenticationResponse {
    String token;
    boolean authenticated;
}
