package com.example.springsecurity.user;

import com.example.springsecurity.user.dto.UserDto;
import com.example.springsecurity.user.role.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setSurname(user.getSurname());
        Set<String> roles = user.getRoles().stream().map(role -> role.getRoleType().getName()).collect(Collectors.toSet());
        userDto.setRoles(roles);
        Set<String> authorities = null;
        userDto.setAuthorities(null);
        return userDto;
    }

    public List<UserDto> toDtoList(List<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

}
