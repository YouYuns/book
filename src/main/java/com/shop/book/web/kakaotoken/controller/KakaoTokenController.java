package com.shop.book.web.kakaotoken.controller;


import com.shop.book.web.kakaotoken.client.KakaoTokenClient;
import com.shop.book.web.kakaotoken.dto.KakaoTokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

    private final KakaoTokenClient kakaoTokenClient;


    @Value("${kakao.client.id}")
    private String clientId;
    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/oauth/kakao/callback")
    @ResponseBody
    public String loginCallback(@RequestParam("code") String code){
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri("http://localhost:8080/oauth/kakao/callback")
                .build();
        KakaoTokenDto.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
        return "kakao toekn : " + kakaoToken;
    }
}
