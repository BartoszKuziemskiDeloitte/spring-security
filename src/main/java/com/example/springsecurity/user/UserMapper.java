package com.example.springsecurity.user;

import com.example.springsecurity.user.dto.UserDto;
import com.example.springsecurity.user.role.AuthorityType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setSurname(user.getSurname());
        List<String> roles = user.getRoles().stream().map(role -> role.getRoleType().getName()).collect(Collectors.toList());
        userDto.setRoles(roles);
        List<String> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.addAll(role.getRoleType().getAuthorities()
                    .stream()
                    .map(AuthorityType::getAuthority)
                    .collect(Collectors.toList()));
        });
        userDto.setAuthorities(authorities);
        return userDto;
    }

    public List<UserDto> toDtoList(List<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

}
