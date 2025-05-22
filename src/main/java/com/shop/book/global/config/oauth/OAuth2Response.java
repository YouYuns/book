package com.shop.book.global.config.oauth;

import com.shop.book.domain.member.constant.OAuthType;

public interface OAuth2Response {

    //제공자 (Ex. naver, google, ...)
    OAuthType getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();
}
