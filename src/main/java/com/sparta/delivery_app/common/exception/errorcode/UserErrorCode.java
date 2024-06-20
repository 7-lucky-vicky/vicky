package com.sparta.delivery_app.common.exception.errorcode;

import com.sparta.delivery_app.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    DUPLICATED_USER(StatusCode.BAD_REQUEST.getCode(), "중복된 유저 정보입니다."),
    ;

    private final Integer httpStatusCode;
    private final String description;
}

