package com.shop.book.api.token.controller;


import com.shop.book.api.token.service.RefreshTokenService;
import com.shop.book.domain.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity tokenRefresh(@RequestParam String accessToken, @RequestParam String refreshToken) throws ParseException {
        String result = refreshTokenService.requestNewAccessToken(accessToken, refreshToken);

        if(result.equals(HttpStatus.UNAUTHORIZED.toString())) // refresh token이 만료된 경우
            return new ResponseEntity<>(new ResponseDto<>(-1,  "만료된 refresh token", null), HttpStatus.UNAUTHORIZED);
        else// refresh token 사용해서 정상 재발급 된 경우
            return new ResponseEntity<>(new ResponseDto<>(1, "Access token 재발급 성공", result), HttpStatus.OK);


    }
}
