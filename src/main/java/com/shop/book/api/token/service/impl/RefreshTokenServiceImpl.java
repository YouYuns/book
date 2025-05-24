package com.shop.book.api.token.service.impl;

import com.shop.book.api.token.dto.RefreshToeknRequestDto;
import com.shop.book.api.token.service.RefreshTokenService;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import com.shop.book.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtUtil jwtUtil;
    @Override
    public RefreshToeknRequestDto refreshToken(String refreshToken) {
        // refresh token 유효성 검증
        checkRefreshToken(refreshToken);
        return null;
    }

    @Override
    public String requestNewAccessToken(String accessToken, String refreshToken) throws ParseException {
        return jwtUtil.validateRefreshToken(accessToken, refreshToken);
    }

    /**
     * refresh token 검증
     *
     * @param refreshToken refresh token
     */
    private void checkRefreshToken(final String refreshToken) {
        if(Boolean.FALSE.equals(jwtUtil.validateToken(refreshToken)))
            new BusinessException(ErrorCode.REFRESH_TOKEN_INVALD);
    }
}
