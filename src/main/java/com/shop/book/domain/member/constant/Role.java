package com.shop.book.domain.member.constant;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER("구매자"),
    ADMIN("관리자");

    private final String roleName;

    public final String roleName(){
        return roleName;
    }
}
