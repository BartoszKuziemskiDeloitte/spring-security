package com.example.springsecurity.security;

import lombok.Getter;

@Getter
public enum UserAuthorities {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String authority;

    UserAuthorities(String authority) {
        this.authority = authority;
    }
}
