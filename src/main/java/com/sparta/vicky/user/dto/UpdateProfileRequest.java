package com.sparta.vicky.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "email 형식이 맞지 않습니다.")
    private String email;

    private String introduce;

}
