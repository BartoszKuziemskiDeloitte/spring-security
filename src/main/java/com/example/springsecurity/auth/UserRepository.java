package com.example.springsecurity.auth;

import java.util.Optional;

public interface UserRepository {

    public Optional<User> selectApplicationUserByUsername(String username);

}
