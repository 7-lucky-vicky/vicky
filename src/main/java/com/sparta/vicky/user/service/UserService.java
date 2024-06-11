package com.sparta.vicky.user.service;

import com.sparta.vicky.jwt.RefreshTokenRepository;
import com.sparta.vicky.user.dto.*;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.entity.UserStatus;
import com.sparta.vicky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원 가입
     */
    @Transactional
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
    public Long logout(User user) {
        deleteRefreshToken(user);
        return user.getId();
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public Long withdraw(WithdrawRequest request, Long id) {
        User user = getUser(id);
        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new IllegalArgumentException("이미 탈퇴한 사용자입니다.");
        }
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            deleteRefreshToken(user);
            user.withdraw();
            return user.getId();
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * Refresh 토큰 삭제
     */
    @Transactional
    public void deleteRefreshToken(User user) {
        String username = user.getUsername();
        refreshTokenRepository.findByUsername(username).ifPresent(refreshTokenRepository::delete);
    }

    /**
     * 프로필 조회
     */
    public ProfileResponse getProfile(Long id) {
        return new ProfileResponse(getUser(id));
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public ProfileResponse updateProfile(Long id, ProfileRequest request) {
        User user = getUser(id);
        user.verifyUser(id);
        user.updateProfile(request);

        return new ProfileResponse(user);
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public ProfileResponse updatePassword(Long id, UpdatePasswordRequest request) {
        User user = getUser(id);
        user.verifyUser(id);
        validatePassword(request.getOldPassword(), user.getPassword());
        validateDuplicatePassword(request.getNewPassword(), user.getPassword());

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        return new ProfileResponse(user);
    }

    /**
     * 비밀번호 검증
     */
    public void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateDuplicatePassword(String newPassword, String currentPassword) {
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new IllegalArgumentException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.");
        }
    }

}
