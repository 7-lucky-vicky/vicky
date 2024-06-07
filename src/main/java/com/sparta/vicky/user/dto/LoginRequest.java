package com.sparta.vicky.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
