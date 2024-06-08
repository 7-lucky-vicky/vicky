package com.sparta.vicky.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawRequest {

    @NotBlank
    private String password;

}
