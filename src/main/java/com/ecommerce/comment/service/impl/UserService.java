package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.dto.request.UserRequest;
import com.ecommerce.comment.dto.response.UserResponse;
import com.ecommerce.comment.entity.User;
import com.ecommerce.comment.exception.ResourceNotFoundException;
import com.ecommerce.comment.repository.UserRepository;
import com.ecommerce.comment.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// Handle all login of user entity
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Description: Add new user to system
     * @param userRequest : information about user
     * @return new user to system
     */
    @Override
    public User addNewUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setFullName(userRequest.getFullName());
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
        user.setPassword(userRequest.getPassword());
        user.setFullName(userRequest.getFullName());
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
