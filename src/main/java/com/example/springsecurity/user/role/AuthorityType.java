package com.example.springsecurity.user.role;

import lombok.Getter;

@Getter
public enum AuthorityType {
    USER_READ("USER_READ"),
    USER_READ_ALL("USER_READ_ALL"),
    ADD_ROLE("ADD_ROLE"),
    USER_DELETE("USER_DELETE");

    private final String authority;

    AuthorityType(String authority) {
        this.authority = authority;
    }
}
