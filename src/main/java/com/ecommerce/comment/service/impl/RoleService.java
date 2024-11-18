package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.dto.request.RoleRequest;
import com.ecommerce.comment.dto.response.PermissionResponse;
import com.ecommerce.comment.dto.response.RoleResponse;
import com.ecommerce.comment.entity.Permission;
import com.ecommerce.comment.entity.Role;
import com.ecommerce.comment.exception.ResourceNotFoundException;
import com.ecommerce.comment.repository.PermissionRepository;
import com.ecommerce.comment.repository.RoleRepository;
import com.ecommerce.comment.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// Handle all logic related to role entity
@Service
public class RoleService implements IRoleService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    /**
     * Create a new role in system
     * @param roleRequest: name and description
     * @return new role in system
     */
    @Override
    public RoleResponse create(RoleRequest roleRequest) {
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        var role = new Role();
        role.setDescription(roleRequest.getDescription());
        role.setName(roleRequest.getName());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return toRoleResponse(role);
    }

    /**
     *
     * @return list role
     */
    @Override
    public List<RoleResponse> findAll() {
        var roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = new ArrayList<>();
        for(Role role : roles) {
            RoleResponse roleResponse = toRoleResponse(role);
            roleResponse.setPermissions(role.getPermissions());
            roleResponses.add(roleResponse);
        }
        return roleResponses;
    }

    /**
     * Delete role by role name
     * @param role: role of user
     */
    @Override
    public void deleteRole(String role) {
        var roles = roleRepository.findById(role).orElseThrow(() -> new ResourceNotFoundException("Role not found !!"));
        roleRepository.deleteById(role);
    }

    // Convert role entity to role response for user
    private RoleResponse toRoleResponse(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setDescription(role.getDescription());
        roleResponse.setName(role.getName());
        roleResponse.setPermissions(role.getPermissions());
        return roleResponse;
    }
}
