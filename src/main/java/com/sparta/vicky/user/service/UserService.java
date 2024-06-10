package com.sparta.vicky.user.service;

import com.sparta.vicky.jwt.JwtProvider;
import com.sparta.vicky.user.dto.*;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 회원 가입
     */
    public User signup(SignupRequest request) {
        // 사용자 ID 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 ID 입니다.");
        }
        // 비밀번호 인코딩 & 사용자 생성
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request, encodedPassword);

        return userRepository.save(user);
    }

    /**
     * 회원 찾기
     */
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("userId가 " + id + " 인 사용자가 존재하지 않습니다."));
    }

    /**
     * 로그아웃
     */
    @Transactional
    public Long logout(Long id) {
        User user = getUser(id);
        //로그아웃 시 refresh 토큰 삭제
        user.saveRefreshToken("");

        return user.getId();
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public Long withdraw(WithdrawRequest request, Long userId) {
        User user = getUser(userId);
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.withdraw();
            return user.getId();
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 토큰 재발급
     */
    @Transactional
    public User reissue(HttpServletRequest request, HttpServletResponse response, Long id) {
        User user = getUser(id);
        String token  = jwtProvider.getRefreshTokenFromHeader(request);

        if (StringUtils.hasText(token)) {
            if (!jwtProvider.validateToken(token)) {
                log.error("Token Error");
            }

            if(token.equals(user.getRefreshToken().substring(7))){
                String accessToken = jwtProvider.createAccessToken(user.getUsername());
                String refreshToken = jwtProvider.createRefreshToken(user.getUsername());

                response.addHeader(JwtProvider.AUTHORIZATION_ACCESS_HEADER, accessToken);
                response.addHeader(JwtProvider.AUTHORIZATION_REFRESH_HEADER, refreshToken);

                // 사용자 개인 필드에 refreshToken 저장
                saveRefreshToken(refreshToken, user.getId());

            }
        }
        return user;
    }

    /**
     * 프로필 조회
     */
    public ProfileResponse getProfile(Long id) {
        User user = getUser(id);

        return new ProfileResponse(user.getUsername(), user.getName(), user.getEmail(), user.getIntroduce());
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public ProfileResponse updateProfile( UpdateProfileRequest request, Long id) {
        User user = getUser(id);
        user.updateProfile(request.getName(), request.getEmail(), request.getIntroduce());

        return new ProfileResponse(user.getUsername(), user.getName(), user.getEmail(), user.getIntroduce());
    }

    /**
     * 비밀번호 수정
     */
    public ProfileResponse updatePassword(UpdatePasswordRequest request, Long id) {
        User user = getUser(id);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        user.updatePassword(request.getNewPassword());

        return new ProfileResponse(user.getUsername(), user.getName(), user.getEmail(), user.getIntroduce());
    }

    /**
     * refresh 토큰 저장
     */
    @Transactional
    public void saveRefreshToken(String refreshToken, Long userId) {
        User user = getUser(userId);
        user.saveRefreshToken(refreshToken);
    }

}
