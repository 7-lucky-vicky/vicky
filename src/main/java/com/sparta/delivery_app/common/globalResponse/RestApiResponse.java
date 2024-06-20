package com.sparta.delivery_app.common.globalResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class RestApiResponse<T> {
    private final boolean success;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    protected RestApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> RestApiResponse<T> of(T data) {
        return new RestApiResponse<>(true, 200, "성공", data);
    }

    public static <T> RestApiResponse<T> of(String message) {
        return new RestApiResponse<>(true, 200, message, null);
    }

    public static <T> RestApiResponse<T> of(String message, T data) {
        return new RestApiResponse<>(true, 200, message, data);
    }

    public static <T> RestApiResponse<T> of(int code, T data) {
        return new RestApiResponse<>(true, code, "성공", data);
    }

    public static <T> RestApiResponse<T> of(int code, String message) {
        return new RestApiResponse<>(true, code, message, null);
    }
}
