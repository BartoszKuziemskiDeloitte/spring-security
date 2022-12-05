package com.example.springsecurity.user.service;

import com.example.springsecurity.user.User;
import com.example.springsecurity.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    List<UserDto> getUsers();

    User addUserRole(String username, String roleName);

}
