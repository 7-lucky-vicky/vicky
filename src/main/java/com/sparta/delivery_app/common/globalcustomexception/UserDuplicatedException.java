package com.sparta.delivery_app.common.globalcustomexception;

import com.sparta.delivery_app.common.exception.errorcode.ErrorCode;

public class UserDuplicatedException extends GlobalDuplicatedException {

    public UserDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
