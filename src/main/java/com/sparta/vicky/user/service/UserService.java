package com.sparta.vicky.user.service;

import com.sparta.vicky.user.dto.SignupRequest;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * 회원 탈퇴
     */
    public Long withdraw(String password, User user) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.withdraw();
            return user.getId();
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 회원 찾기
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("userId가 " + id + " 인 사용자가 존재하지 않습니다."));
    }

}
