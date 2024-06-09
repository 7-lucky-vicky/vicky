package com.sparta.vicky.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdatePassword {

    @NotBlank(message = "현재 사용 중인 비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$",
            message = "비밀번호는 최소 10자 이상이어야 하며, 영문 대소문자, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String oldPassword;

    @NotBlank(message = "변경할 비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$",
            message = "비밀번호는 최소 10자 이상이어야 하며, 영문 대소문자, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String newPassword;

}
