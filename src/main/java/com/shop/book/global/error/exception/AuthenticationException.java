package com.shop.book.global.error.exception;

import com.shop.book.global.error.ErrorCode;

public class AuthenticationException extends BusinessException{
    public AuthenticationException(ErrorCode errorCode){
        super(errorCode);
    }
}
