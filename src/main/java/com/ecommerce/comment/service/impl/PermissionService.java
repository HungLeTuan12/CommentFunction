package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.dto.request.PermissionRequest;
import com.ecommerce.comment.dto.response.PermissionResponse;
import com.ecommerce.comment.entity.Permission;
import com.ecommerce.comment.repository.PermissionRepository;
import com.ecommerce.comment.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Handle all logic related to permission entity
@Service
public class PermissionService implements IPermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    /**
     * Create a new permission in system
     * @param request: name and description
     * @return new permission in system
     */
    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission = permissionRepository.save(permission);
        return toPermissionResponse(permission);
    }



    /**
     *
     * @return list permission in system
     */
    @Override
    public List<PermissionResponse> findAll() {
        var permissions = permissionRepository.findAll();
        List<PermissionResponse> permissionResponses = new ArrayList<>();
        for(Permission permission : permissions) {
            PermissionResponse permissionResponse = toPermissionResponse(permission);
            permissionResponses.add(permissionResponse);
        }
        return permissionResponses;
    }

    /**
     * Delete permission in system
     * @param permission:
     */
    @Override
    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }
    // Convert permission entity to permission response for user
    private PermissionResponse toPermissionResponse(Permission permission) {
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setDescription(permission.getDescription());
        permissionResponse.setName(permission.getName());
        return permissionResponse;
    }
}
