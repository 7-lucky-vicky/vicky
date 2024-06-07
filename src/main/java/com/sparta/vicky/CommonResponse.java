package com.sparta.vicky;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse<T> {

    private int statusCode;
    private String msg;
    private T data;
}
