package com.shop.book.global.error;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse{
    private String errorCode;
    private String errorMessage;
    public static ErrorResponse of(String errorCode, String errorMessage){
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
    //BindingResult에 Error정보들이 들어간다.
    public static ErrorResponse of(String errorCode, BindingResult bindingResult){
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(createErrorMessage(bindingResult))
                .build();
    }

    private static String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            //첫번째 에러 메세지가 아니면 ", " 단위로 연결을 해준다
            if(!isFirst){
                sb.append(", ");
            }else{
                isFirst = false;
            }
            sb.append("[");
            //에러의 필드명
            sb.append(fieldError.getField());
            sb.append("]");
            //에러의 기본 메세지
            sb.append(fieldError.getDefaultMessage());
        }
        return sb.toString();
    }

}
