package com.sparta.vicky.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    //회원가입RequestDto
    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min=10, max=20, message="아이디는 10자에서 20자 사이입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디 형식이 맞지 않습니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min=10)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$",
            message = "비밀번호 형식이 맞지 않습니다."
    )
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message="email형식이 맞지 않습니다.")
    private String email;

    private String introduce;

}
