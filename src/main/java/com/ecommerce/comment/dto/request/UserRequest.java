package com.ecommerce.comment.dto.request;

import com.ecommerce.comment.dto.response.RoleResponse;
import com.ecommerce.comment.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// This class handle entity request for receiving data for client
public class UserRequest {
    private Long id;
    @NotNull(message = "Username can not null")
    private String username;
    @NotNull(message = "Password can not null")
    @Length(min = 6)
    private String password;
    @NotNull(message = "Full name can not null")
    @Length(min = 6)
    private String fullName;
    @Email
    @NotNull(message = "Email can not null")
    private String email;
    Set<String> role;
}
