package com.shop.book.api.service;

import com.shop.book.domain.member.dto.MemberDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    void memberSignup(MemberDto memberDto);

    ResponseEntity<?> memberLogin(MemberDto.MemberLoginReqDto memberLoginReqDto);
}
