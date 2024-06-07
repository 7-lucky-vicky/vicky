package com.sparta.vicky.user.dto;

import com.sparta.vicky.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignupResponse {

    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String email;
    private final String introduce;
    private final LocalDateTime createdAt;

    public SignupResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
        this.createdAt = user.getCreatedAt();
    }

}