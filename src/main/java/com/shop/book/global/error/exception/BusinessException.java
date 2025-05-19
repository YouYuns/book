package com.shop.book.global.error.exception;

import com.shop.book.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        //부모에 있던 에러 메세지를 넣어준다.
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
