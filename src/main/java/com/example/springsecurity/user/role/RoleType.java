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
    USER("ROLE_USER", Sets.newHashSet()),
    ADMIN("ROLE_ADMIN", Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    SUPERADMIN("ROLE_SUPERADMIN", Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final String name;
    private final Set<AuthorityType> authorities;

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
        //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }

}
