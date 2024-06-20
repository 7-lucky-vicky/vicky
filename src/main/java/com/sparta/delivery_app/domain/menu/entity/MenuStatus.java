package com.sparta.delivery_app.domain.menu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuStatus {
    ENABLE("ENABLE"),
    DISABLE("DISABLE");

    private final String menuStatusName;
}
