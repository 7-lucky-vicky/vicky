package com.sparta.delivery_app.common.globalResponse;


import com.sparta.delivery_app.common.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse<T> extends RestApiResponse {

    protected ErrorResponse(boolean success, int code, String message) {
        super(success, code, message, null);
    }

    protected ErrorResponse(boolean success, int code, String message, T data) {
        super(success, code, message, data);
    }


    public static <T> ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse<>(false,
                errorCode.getHttpStatusCode(),
                errorCode.getDescription());
    }

    public static <T> ErrorResponse of(ErrorCode errorCode, T data) {
        return new ErrorResponse<>(false,
                errorCode.getHttpStatusCode(),
                errorCode.getDescription(),
                data);
    }
}
