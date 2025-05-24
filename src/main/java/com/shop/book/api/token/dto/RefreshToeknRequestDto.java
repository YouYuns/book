package com.shop.book.api.token.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RefreshToeknRequestDto {
    private String refreshToken;
}
