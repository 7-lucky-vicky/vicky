package com.sparta.delivery_app.domain.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreStatus {

    ENABLE("ENABLE"),
    DISABLE("DISABLE");
    private final String storeStatusName;

}
