package com.sparta.vicky.comment.controller;

import com.sparta.vicky.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Slf4j(topic = "Controller")
public final class ControllerUtils {

    static ResponseEntity<CommonResponse<?>> getFieldErrorResponseEntity(BindingResult bindingResult, String msg) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.error("{} field : {}", fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(CommonResponse.<List<FieldError>>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .msg(msg)
                        .data(fieldErrors)
                        .build());
    }

    static ResponseEntity<CommonResponse<?>> getBadRequestResponseEntity(Exception e) {
        return ResponseEntity.badRequest().body(CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(e.getMessage())
                .build());
    }

    static ResponseEntity<CommonResponse<?>> getResponseEntity(Object response, String msg) {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg(msg)
                .data(response)
                .build());
    }

}
