package com.example.springsecurity.auth;

import java.util.Optional;

public interface ApplicationUserDao {

    public Optional<User> selectApplicationUserByUsername(String username);

}
