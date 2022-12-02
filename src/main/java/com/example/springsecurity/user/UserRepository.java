package com.example.springsecurity.user;

import java.util.Optional;

public interface UserRepository {

    Optional<UserPrincipal> selectApplicationUserByUsername(String username);

}
