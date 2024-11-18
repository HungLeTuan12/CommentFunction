package com.ecommerce.comment.controller;

import com.ecommerce.comment.dto.request.AuthenticateRequest;
import com.ecommerce.comment.dto.response.AuthenticationResponse;
import com.ecommerce.comment.response.ErrorResponse;
import com.ecommerce.comment.response.SuccessResponse;
import com.ecommerce.comment.service.impl.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
// Handle all endpoint for authentication
public class AuthenticateController {
    @Autowired
    private AuthenticationService authenticationService;
    @Operation(summary = "Check validation log - in",
            description = "Log-in includes username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log-in Successfully"),
            @ApiResponse(responseCode = "404", description = "Cannot find by username"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/log-in")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticateRequest authenticateRequest) throws JOSEException {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticateRequest);
        return ResponseEntity.ok(new SuccessResponse<>("Authenticate success",authenticationResponse));
    }

}
