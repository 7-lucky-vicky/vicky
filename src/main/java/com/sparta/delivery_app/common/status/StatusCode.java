package com.sparta.delivery_app.common.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    OK(200),
    CREATED(201),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),

    INTERNAL_SERVER_ERROR(500),
    ;

    public final int code;

}
