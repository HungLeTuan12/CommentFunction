package com.ecommerce.comment.controller;

import com.ecommerce.comment.dto.request.UserRequest;
import com.ecommerce.comment.dto.response.CommentResponse;
import com.ecommerce.comment.dto.response.UserResponse;
import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.entity.User;
import com.ecommerce.comment.response.SuccessResponse;
import com.ecommerce.comment.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    // Add user function
    @Operation(summary = "Get all users from system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all users successfully"),
            @ApiResponse(responseCode = "500", description = "Get all users failed")
    })
    @GetMapping("/find-all")
    public ResponseEntity<?> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user : users) {
            UserResponse userResponse = convertEntityToResponse(user);
            userResponses.add(userResponse);
        }
        return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",userResponses));
    }
    // Add user function
    @Operation(summary = "Add new user")
    @Parameter(description = "Request payload to add a new user", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add new user successfully"),
            @ApiResponse(responseCode = "500", description = "Adding user failed")
    })
    @PostMapping("/")
    public ResponseEntity<?> addNewUser(@Parameter(description = "Des")@RequestBody @Valid UserRequest userRequest) {
        User user = userService.addNewUser(userRequest);
        UserResponse userResponse = convertEntityToResponse(user);
        return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",userResponse));
    }
    // Update user function
    @Operation(summary = "Update user information")
    @Parameter(description = "Request payload to add a new user", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user information successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Update user failed")
    })
    @PostMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserRequest userRequest) {
        User user = userService.updateUser(userRequest);
        UserResponse userResponse = convertEntityToResponse(user);
        return ResponseEntity.ok(new SuccessResponse<>("Update user information successfully",userResponse));
    }
    // Delete user
    @Operation(summary = "Delete user by id")
    @Parameter(description = "Request payload to add a new user", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete user successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Delete user failed")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new SuccessResponse<>("Delete user successfully"));
    }
    // Convert entity comment from database to comment response for user
    public UserResponse convertEntityToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setFullName(user.getFullName());
        userResponse.setPassword(user.getPassword());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(user.getRole());
        return userResponse;
    }
}
