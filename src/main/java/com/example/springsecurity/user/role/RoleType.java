package com.example.springsecurity.user.role;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.springsecurity.user.role.AuthorityType.*;

@Getter
@AllArgsConstructor
public enum RoleType {
    ROLE_USER("ROLE_USER", Sets.newHashSet(USER_READ)),
    ROLE_ADMIN("ROLE_ADMIN", Sets.newHashSet(USER_READ_ALL)),
    ROLE_SUPERADMIN("ROLE_SUPERADMIN", Sets.newHashSet(ADD_ROLE, USER_DELETE));

    private final String name;

    // @Transient ??
    private final Set<AuthorityType> authorities;

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
    }

}
