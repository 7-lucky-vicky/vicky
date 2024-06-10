package com.sparta.vicky.user.dto;

import com.sparta.vicky.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponse {

    private String username;
    private String name;
    private String email;
    private String introduce;

    public ProfileResponse(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
    }

}
