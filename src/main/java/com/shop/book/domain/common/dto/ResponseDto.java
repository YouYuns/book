package com.shop.book.domain.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final Integer code; //1 성공 , -1 실패
    private final String msg;
    private final T data; //데이터가 다양하기 때문에 제네릭으로 만듬


}
