package com.ecommerce.comment.service;

import com.ecommerce.comment.dto.request.RoleRequest;
import com.ecommerce.comment.dto.response.PermissionResponse;
import com.ecommerce.comment.dto.response.RoleResponse;

import java.util.List;

// Handle all logic related to role entity
public interface IRoleService {
    /**
     *
     * @param roleRequest: name and description and permission
     * @return new role in system
     */
    public RoleResponse create(RoleRequest roleRequest);

    /**
     *
     * @return list role
     */
    List<RoleResponse> findAll();

    /**
     * Delete role by role name
     * @param role: role of user
     */
    void deleteRole(String role);
}
