package com.sparta.delivery_app.common.exceptionhandler;

import com.sparta.delivery_app.common.exception.errorcode.CommonErrorCode;
import com.sparta.delivery_app.common.globalResponse.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Order(1)
@RestControllerAdvice
public class ValidExceptionHandler {

    /**
     * 유효성 검사에서 예외가 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] cause : {}, message : {} ", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());

        ConcurrentHashMap<Object, Object> validationMessage = new ConcurrentHashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            validationMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.BAD_REQUEST, validationMessage);

        return ResponseEntity.status(CommonErrorCode.BAD_REQUEST.getHttpStatusCode())
                .body(errorResponse);
    }

}
