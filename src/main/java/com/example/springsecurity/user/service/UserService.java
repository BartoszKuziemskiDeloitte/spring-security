package com.example.springsecurity.user.service;

import com.example.springsecurity.user.User;
import com.example.springsecurity.user.dto.ChangeRolesDto;
import com.example.springsecurity.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserByUsername(String username);

    List<UserDto> getUsers();

    UserDto registerUser(User user);

    UserDto changeRoles(ChangeRolesDto changeRolesDto);

    UserDto deleteUser(String username);
}
