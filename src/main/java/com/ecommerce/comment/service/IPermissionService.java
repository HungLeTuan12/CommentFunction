package com.ecommerce.comment.service;

import com.ecommerce.comment.dto.request.PermissionRequest;
import com.ecommerce.comment.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    /**
     *
     * @param request: name and description
     * @return new permission in system
     */
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> findAll();
    void deletePermission(String permission);
}
