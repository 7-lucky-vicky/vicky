package com.sparta.vicky.config;

import com.sparta.vicky.user.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Slf4j(topic = "Controller")
public final class ControllerUtils {

     public static ResponseEntity<CommonResponse<?>> getFieldErrorResponseEntity(BindingResult bindingResult, String msg) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.error("{} field : {}", fieldError.getField(), fieldError.getDefaultMessage());
        }

        return org.springframework.http.ResponseEntity.badRequest()
                .body(CommonResponse.<List<FieldError>>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .msg(msg)
                        .data(fieldErrors)
                        .build());
    }

     public static ResponseEntity<CommonResponse<?>> getBadRequestResponseEntity(Exception e) {
        return ResponseEntity.badRequest().body(CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(e.getMessage())
                .build());
    }

     public static ResponseEntity<CommonResponse<?>> getResponseEntity(Object response, String msg) {
        return org.springframework.http.ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg(msg)
                .data(response)
                .build());
    }

}
