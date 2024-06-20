package com.sparta.delivery_app.common.exceptionhandler;

import com.sparta.delivery_app.common.exception.errorcode.CommonErrorCode;
import com.sparta.delivery_app.common.globalcustomexception.GlobalDuplicatedException;
import com.sparta.delivery_app.common.exception.errorcode.ErrorCode;
import com.sparta.delivery_app.common.globalResponse.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "ApiException")
@Order(1)
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Api 요청에 동작 중 예외가 발생한 경우
     */
    @ExceptionHandler(GlobalDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> apiException(GlobalDuplicatedException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ErrorResponse.of(errorCode));
    }

    /**
     * IllegalArgumentException
     * @@@@@@@@@@
     */
//    @ExceptionHandler(IllegalArgumentException.class)
//    protected ResponseEntity<ErrorResponse> apiException(IllegalArgumentException e) {
//        e.getMessage() //
//        return ResponseEntity.status()
//                .body(ErrorResponse.of());
//    }

}
