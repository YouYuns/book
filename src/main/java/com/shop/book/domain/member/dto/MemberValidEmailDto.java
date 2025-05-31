package com.shop.book.domain.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberValidEmailDto {
    private String email;
    private String code;
}
