package com.sparta.delivery_app.common.exception.errorcode;

public interface ErrorCode {
    Integer getHttpStatusCode();

    String getDescription();
}
