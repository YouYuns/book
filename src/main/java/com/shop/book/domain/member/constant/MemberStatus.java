package com.shop.book.domain.member.constant;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberStatus {

    ACTIVE("활성"),
    PENDING("대기"),
    BLOCKED("정지");

    private final String typeName;

    public final String typeName(){
        return typeName;
    }

}
