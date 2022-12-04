package com.example.springsecurity.user.service;

import com.example.springsecurity.user.User;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    List<User> getUsers();

    User addUserRole(String username, String roleName);

}
