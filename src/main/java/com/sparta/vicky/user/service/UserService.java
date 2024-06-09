package com.sparta.vicky.user.service;

import com.sparta.vicky.user.dto.*;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
     * 로그아웃
     */
    @Transactional
    public Long logout(Long id) {
        User user = getUser(id);
        // 로그아웃 시 refresh 토큰 비워주기
        user.saveRefreshToken("");

        return user.getId();
    }

    /**
     * 프로필 조회
     */
    public ProfileResponse getProfile(Long id) {
        User user = getUser(id);
        return new ProfileResponse(user.getName(), user.getEmail(), user.getIntroduce());
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request, Long id) {
        User user = getUser(id);
        user.updateProfile(request.getName(), request.getEmail(), request.getIntroduce());

        return new ProfileResponse(user.getName(), user.getEmail(), user.getIntroduce());
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public ProfileResponse updatePassword(UpdatePassword request, Long id) {
        User user = getUser(id);

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(request.getNewPassword());

        return new ProfileResponse(user.getName(), user.getEmail(), user.getIntroduce());
    }

    /**
     * refresh 토큰 저장
     */
    @Transactional
    public void saveRefreshToken(String refreshToken, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Not Found User")
        );
        user.saveRefreshToken(refreshToken);
    }

    /**
     * refresh 토큰으로 user 조회
     */
    public boolean checkRefreshToken(String refreshToken, String username) {
        Optional<User> user = userRepository.findByUsernameAndRefreshToken(username, refreshToken);
        return user.isPresent();

    }

}
