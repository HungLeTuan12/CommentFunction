package com.ecommerce.comment.controller;

import com.ecommerce.comment.dto.request.CommentRequest;
import com.ecommerce.comment.dto.request.PermissionRequest;
import com.ecommerce.comment.dto.response.CommentResponse;
import com.ecommerce.comment.dto.response.PermissionResponse;
import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.response.SuccessResponse;
import com.ecommerce.comment.service.impl.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@Slf4j
// Handle all requests about permission
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    // Get all permissions function
    @Operation(summary = "Retrieve all permissions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllPermissions() {
        List<PermissionResponse> permissionResponses = permissionService.findAll();
        return ResponseEntity.ok(new SuccessResponse<>("Get success",permissionResponses));
    }

    // Create permission function
    @Operation(summary = "Add new permission")
    @Parameter(description = "Request payload to add a new permission", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add new permission successfully"),
            @ApiResponse(responseCode = "500", description = "Adding permission failed")
    })
    // Add new comment, demo
    @PostMapping("/add-permission")
    public ResponseEntity<?> addNewPermission(@RequestBody @Valid PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = permissionService.create(permissionRequest);
        return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",permissionResponse));
    }

    // Delete permission function
    @Operation(summary = "Delete permission")
    @Parameter(description = "Delete permission !!", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete permission successfully"),
            @ApiResponse(responseCode = "500", description = "Delete permission failed")
    })
    // Add new comment, demo
    @DeleteMapping("/{permission}")
    public ResponseEntity<?> deletePermission(@PathVariable String permission) {
        permissionService.deletePermission(permission);
        return ResponseEntity.ok(new SuccessResponse<>("Delete successfully !!"));
    }
}
