package com.example.springsecurity.user.service;

import com.example.springsecurity.user.User;
import com.example.springsecurity.user.UserMapper;
import com.example.springsecurity.user.UserPrincipal;
import com.example.springsecurity.user.UserRepository;
import com.example.springsecurity.user.dto.AddRoleDto;
import com.example.springsecurity.user.dto.UserDto;
import com.example.springsecurity.user.role.Role;
import com.example.springsecurity.user.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springsecurity.user.role.RoleType.ROLE_USER;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new UserPrincipal(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto registerUser(User user) {
        Role userRole = roleRepository.findRoleByRoleType(ROLE_USER).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.setRoles(List.of(userRole));
        User userToSave = userRepository.save(user);
        return userMapper.toDto(userToSave);
    }

    @Override
    public UserDto addUserRole(AddRoleDto addRoleDto) {
        String username = addRoleDto.getUsername();
        List<String> roles = addRoleDto.getRoles();

        List<Role> allRoles = roleRepository.findAll();
        List<Role> allRolesForUser = new ArrayList<>();
        roles.forEach(roleStr -> {
            allRolesForUser.addAll(
                    allRoles.stream()
                            .filter(role -> role.getRoleType().getName().equals(roleStr))
                            .collect(Collectors.toList()));
        });
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRoles(allRolesForUser);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto deleteUser(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        return userMapper.toDto(user);
    }
}
