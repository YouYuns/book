package com.shop.book.api.member.service;

import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.member.dto.MemberEmailSendDto;
import com.shop.book.global.jwt.JwtDto;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface MemberService {
    void memberSignup(MemberDto memberDto);

    JwtDto memberLogin(MemberDto.MemberLoginReqDto memberLoginReqDto) throws ParseException;

    boolean isEmailDuplicate(String email);

    String memberSendEmail(MemberEmailSendDto memberEmailSendDto);

}
