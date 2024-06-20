package com.sparta.delivery_app.domain.review.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewStatus {

    ENABLE("ENABLE"),
    DISABLE("DISABLE");
    private final String reviewStatusName;
}
