package com.example.springsecurity.user;

import com.example.springsecurity.user.role.RoleType;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        this.user.getRoles().forEach(role -> {
            String roleName = role.getRoleType();
            RoleType roleType = RoleType.valueOf(roleName);
            Set<GrantedAuthority> authoritiesFromRole = roleType.getGrantedAuthorities();
            grantedAuthorities.addAll(authoritiesFromRole);
        });

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
