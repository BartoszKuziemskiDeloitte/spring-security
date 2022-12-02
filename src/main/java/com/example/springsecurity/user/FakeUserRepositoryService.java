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
    public Optional<User> selectApplicationUserByUsername(String username) {
        return getApplicationUsers().stream().filter(user -> username.equals(user.getUsername())).findFirst();
    }

    private List<User> getApplicationUsers() {
        return Lists.newArrayList(
                new User(
                        "bartek",
                        passwordEncoder.encode("password"),
                        STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new User(
                        "admin",
                        passwordEncoder.encode("password"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new User(
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
