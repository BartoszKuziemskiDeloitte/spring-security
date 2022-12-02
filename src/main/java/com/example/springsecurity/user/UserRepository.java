package com.example.springsecurity.user;

import java.util.Optional;

public interface UserRepository {

    public Optional<User> selectApplicationUserByUsername(String username);

}
