package com.ecommerce.comment.service;

import com.ecommerce.comment.dto.request.UserRequest;
import com.ecommerce.comment.dto.response.UserResponse;
import com.ecommerce.comment.entity.User;

import java.util.List;

// Handle all logic about user entity
public interface IUserService {
    /**
     * Description: Get all users from system
     * @return the list includes all users information
     */
    public List<User> getAllUsers();
    /**
     * Description: Add new user to system
     * @param userRequest : information about user
     * @return the new user was added
     */
    public User addNewUser(UserRequest userRequest);

    /**
     * Description: Edit user ìnformation
     * @param userRequest: information about user
     * @return the new user was edited and save new information in system
     */
    public User updateUser(UserRequest userRequest);

    /**
     * Description: Delete user from system
     * @param userId: id of user
     */
    public void deleteUser(Long userId);

}
