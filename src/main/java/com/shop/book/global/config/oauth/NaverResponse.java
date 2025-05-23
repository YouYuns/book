package com.shop.book.global.config.oauth;

import com.shop.book.domain.member.constant.OAuthType;

import java.util.Map;

public class NaverResponse implements OAuth2Response{
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public OAuthType getProvider() {
        return OAuthType.NAVER;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
