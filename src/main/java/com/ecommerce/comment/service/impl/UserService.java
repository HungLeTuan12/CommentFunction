package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.constant.Role;
import com.ecommerce.comment.dto.request.UserRequest;
import com.ecommerce.comment.dto.response.UserResponse;
import com.ecommerce.comment.entity.User;
import com.ecommerce.comment.exception.ResourceNotFoundException;
import com.ecommerce.comment.repository.RoleRepository;
import com.ecommerce.comment.repository.UserRepository;
import com.ecommerce.comment.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
@Service
@Slf4j
// Handle all login of user entity
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Description: Add new user to system
     * @param userRequest : information about user
     * @return new user to system
     */
    @Override
    public User addNewUser(UserRequest userRequest) {
        System.out.println("--------- " + userRequest.getUsername());
        User user = userRepository.findByUsername(userRequest.getUsername())
                        .orElseThrow(() -> new RuntimeException("Username is exist, please try again !"));
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        var roles = roleRepository.findAllById(userRequest.getRole());
        user.setRole(new HashSet<>(roles));
        return userRepository.save(user);
    }
    /**
     * Description: Update user information
     * @param userRequest: information about user
     * @return new information for user
     */
    @Override
    public User updateUser(UserRequest userRequest) {
        User user = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id "+ userRequest.getId() + " not found !!"));
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        var roles = roleRepository.findAllById(userRequest.getRole());
        user.setRole(new HashSet<>(roles));
        return userRepository.save(user);
    }

    /**
     * Description: Delete user from system
     * @param userId: id of user
     */
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id "+ userId + " not found !!"));
        userRepository.deleteById(userId);
    }
}
