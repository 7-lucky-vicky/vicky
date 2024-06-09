package com.sparta.vicky.user.dto;

import lombok.Data;

@Data
public class ProfileResponse {

    private String username;
    private String name;
    private String email;
    private String introduce;

    public ProfileResponse(String username, String name, String email, String introduce) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.introduce = introduce;
    }

}
