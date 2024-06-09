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
     * 회원 탈퇴
     */
    @PostMapping("/user/withdraw")
    public ResponseEntity<CommonResponse<?>> withdraw(
            @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Long response = userService.withdraw(request, userDetails.getUser().getId());

            return getResponseEntity(response, "회원 탈퇴 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 로그아웃
     */
    @PatchMapping("/user/logout")
    public ResponseEntity<CommonResponse<?>> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long response = userService.logout(userDetails.getUser().getId());

            return getResponseEntity(response, "로그아웃 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<?>> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            ProfileResponse response = userService.getProfile(userDetails.getUser().getId());

            return getResponseEntity(response, "프로필 조회 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 프로필 수정
     */
    @PatchMapping("/profile")
    public ResponseEntity<CommonResponse<?>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            ProfileResponse response = userService.updateProfile(request, userDetails.getUser().getId());

            return getResponseEntity(response, "프로필 수정 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}
