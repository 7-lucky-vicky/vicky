package com.sparta.delivery_app.common.exception.errorcode;


import com.sparta.delivery_app.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_ARGUMENT_ERROR(StatusCode.BAD_REQUEST.getCode(), "올바르지 않은 파라미터입니다."),
    BAD_REQUEST(StatusCode.BAD_REQUEST.code, "올바르지 않은 요청 정보입니다."),
    ;

    private final Integer httpStatusCode;
    private final String description;
}
