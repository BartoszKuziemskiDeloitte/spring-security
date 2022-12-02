package com.example.springsecurity.user;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import static com.example.springsecurity.security.UserRole.*;

@Repository("fake")
@AllArgsConstructor
public class FakeUserRepositoryService implements UserRepository {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserPrincipal> selectApplicationUserByUsername(String username) {
        return getApplicationUsers().stream().filter(userPrincipal -> username.equals(userPrincipal.getUsername())).findFirst();
    }

    private List<UserPrincipal> getApplicationUsers() {
        return Lists.newArrayList(
                new UserPrincipal(
                        "bartek",
                        passwordEncoder.encode("password"),
                        STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new UserPrincipal(
                        "admin",
                        passwordEncoder.encode("password"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new UserPrincipal(
                        "adam",
                        passwordEncoder.encode("password"),
                        ADMINTRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
    }

}
