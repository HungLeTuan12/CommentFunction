package com.ecommerce.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// This class handle entity request for receiving data for client
public class RoleRequest {
    private String name;
    private String description;
    Set<String> permissions;
}
