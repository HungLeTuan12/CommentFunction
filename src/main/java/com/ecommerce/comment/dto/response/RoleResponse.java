package com.ecommerce.comment.dto.response;

import com.ecommerce.comment.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Response for frontend
public class RoleResponse {
    private String name;
    private String description;
    Set<Permission> permissions;
}
