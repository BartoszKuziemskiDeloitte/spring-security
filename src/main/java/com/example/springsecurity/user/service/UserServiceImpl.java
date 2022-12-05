package com.example.springsecurity.user.service;

import com.example.springsecurity.user.User;
import com.example.springsecurity.user.UserMapper;
import com.example.springsecurity.user.UserPrincipal;
import com.example.springsecurity.user.UserRepository;
import com.example.springsecurity.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new UserPrincipal(user);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return user;
    }

    @Override
    public List<UserDto> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public User addUserRole(String username, String roleName) {
        return null;
    }
}
