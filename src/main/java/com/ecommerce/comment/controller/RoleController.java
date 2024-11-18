package com.ecommerce.comment.controller;

import com.ecommerce.comment.dto.request.PermissionRequest;
import com.ecommerce.comment.dto.request.RoleRequest;
import com.ecommerce.comment.dto.response.PermissionResponse;
import com.ecommerce.comment.dto.response.RoleResponse;
import com.ecommerce.comment.response.SuccessResponse;
import com.ecommerce.comment.service.impl.PermissionService;
import com.ecommerce.comment.service.impl.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;
    // Get all permissions function
    @Operation(summary = "Retrieve all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of roles"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllRoles() {
        List<RoleResponse> roleResponses = roleService.findAll();
        return ResponseEntity.ok(new SuccessResponse<>("Get success",roleResponses));
    }
    // Create role function
    @Operation(summary = "Add new role")
    @Parameter(description = "Request payload to add a new role", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add new role successfully"),
            @ApiResponse(responseCode = "500", description = "Adding role failed")
    })
    // Add new comment, demo
    @PostMapping("/add-role")
    public ResponseEntity<?> addNewRole(@RequestBody @Valid RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.create(roleRequest);
        return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",roleResponse));
    }
    // Delete role function
    @Operation(summary = "Delete role")
    @Parameter(description = "Delete role !!", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete role successfully"),
            @ApiResponse(responseCode = "500", description = "Delete role failed")
    })
    // Add new comment, demo
    @DeleteMapping("/{role}")
    public ResponseEntity<?> deletePermission(@PathVariable String role) {
        roleService.deleteRole(role);
        return ResponseEntity.ok(new SuccessResponse<>("Delete successfully !!"));
    }
}
