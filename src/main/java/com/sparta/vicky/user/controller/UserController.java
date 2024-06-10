package com.sparta.vicky.user.controller;

import com.sparta.vicky.base.dto.CommonResponse;
import com.sparta.vicky.security.UserDetailsImpl;
import com.sparta.vicky.user.dto.*;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.vicky.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입
     */
    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
        }
        try {
            User user = userService.signup(request);
            SignupResponse response = new SignupResponse(user);

            return getResponseEntity(response, "회원 가입 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 로그아웃
     */
    @PostMapping("/user/logout")
    public ResponseEntity<CommonResponse<?>> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Long response = userService.logout(userDetails.getUser());

            return getResponseEntity(response, "로그아웃 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/user/withdraw")
    public ResponseEntity<CommonResponse<?>> withdraw(
            @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Long response = userService.withdraw(request, userDetails.getUser());

            return getResponseEntity(response, "회원 탈퇴 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<?>> getProfile(
            @PathVariable Long userId
    ) {
        try {
            ProfileResponse response = userService.getProfile(userId);

            return getResponseEntity(response, "프로필 조회 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 프로필 수정
     */
    @PostMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<?>> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "프로필 수정 실패");
        }
        try {
            ProfileResponse response = userService.updateProfile(userId, request, userDetails.getUser());

            return getResponseEntity(response, "프로필 수정 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<?>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "비밀번호 변경 실패");
        }
        try {
            ProfileResponse response = userService.updatePassword(userId, request, userDetails.getUser());

            return getResponseEntity(response, "비빌번호 변경 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}
