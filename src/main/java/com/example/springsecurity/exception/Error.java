package com.example.springsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {

    AUTHENTICATION_EXCEPTION("Username and password filter failed to authenticate", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_BLACKLISTED("Refresh token is no longer valid", HttpStatus.FORBIDDEN),
    REFRESH_TOKEN_ALREADY_BLACKLISTED("Refresh token is already blacklisted", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;

}
