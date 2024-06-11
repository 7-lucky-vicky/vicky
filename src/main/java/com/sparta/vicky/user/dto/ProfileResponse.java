package com.sparta.vicky.user.dto;

import com.sparta.vicky.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProfileResponse {

    private Long userId;
    private String username;
    private String name;
    private String email;
    private String introduce;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProfileResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}
