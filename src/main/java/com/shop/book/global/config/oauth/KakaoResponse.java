package com.shop.book.global.config.oauth;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{


    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) attribute.get("profile");
        return String.valueOf(profile.get("nickname"));
    }
}
