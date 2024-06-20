package com.sparta.delivery_app.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ENABLE("ENABLE"),
    DISABLE("DISABLE");

    private final String userStatusName;
}
