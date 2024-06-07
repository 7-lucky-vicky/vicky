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

    public User signup(SignupRequest request) {
        String username = request.getUsername();
        String password = passwordEncoder.encode(request.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username " + username + " already exists");
        }

        User user = new User(username, password, request.getName(), request.getEmail(), request.getIntroduce());
        userRepository.save(user);

        return user;
    }
}
