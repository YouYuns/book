package com.shop.book.api.token.service;

import com.shop.book.api.token.dto.RefreshToeknRequestDto;

import java.text.ParseException;

public interface RefreshTokenService {

    RefreshToeknRequestDto refreshToken(final String refreshToken);

    String requestNewAccessToken(String accessToken, String refreshToken) throws ParseException;
}
