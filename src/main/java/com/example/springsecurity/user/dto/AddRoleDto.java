package com.example.springsecurity.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddRoleDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private List<String> roles;

}
