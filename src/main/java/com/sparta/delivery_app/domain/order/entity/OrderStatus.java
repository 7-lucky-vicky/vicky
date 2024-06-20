package com.sparta.delivery_app.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_COMPLETED("ORDER_COMPLETED"),
    IN_PREPARATION("IN_PREPARATION"),
    DELIVERY_COMPLETED("DELIVERY_COMPLETED");

    private final String orderStatusName;
}
