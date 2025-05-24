package com.shop.book.global.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JwtDto {
    private String grantType;
    private String access_token;
    private String refresh_token;
}
