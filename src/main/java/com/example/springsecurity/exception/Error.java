package com.example.springsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {

    AUTHENTICATION_EXCEPTION("Username and password filter failed to authenticate", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

}