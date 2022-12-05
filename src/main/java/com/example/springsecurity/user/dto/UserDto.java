package com.example.springsecurity.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("roles")
    private Set<String> roles;

    @JsonProperty("authorities")
    private Set<String> authorities;

}
