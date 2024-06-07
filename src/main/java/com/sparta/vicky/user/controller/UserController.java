package com.sparta.vicky.user.controller;

import com.sparta.vicky.user.dto.CommonResponse;
import com.sparta.vicky.user.dto.SignupRequest;
import com.sparta.vicky.user.dto.SignupResponse;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.vicky.user.controller.ControllerUtils.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) throws IllegalArgumentException {
        // 바인딩 예외 처리
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "Failed to signup");
        }
        try {
            User user = userService.signup(request);
            SignupResponse response = new SignupResponse(user);

            return getResponseEntity(response, "Signup successful");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}
