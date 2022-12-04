package com.example.springsecurity.user.role;

import lombok.Getter;

@Getter
public enum AuthorityType {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String authority;

    AuthorityType(String authority) {
        this.authority = authority;
    }
}
