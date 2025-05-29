package com.shop.book.api.member.controller;

import com.shop.book.api.member.service.MemberService;
import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.member.dto.MemberEmailSendDto;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import com.shop.book.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private static final String path = "/member";
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    @PostMapping(path = path + "/signup")
    public ResponseEntity memberSignup(@RequestBody MemberDto memberDto){
        if (memberService.isEmailDuplicate(memberDto.getEmail())) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE);
        }
        memberService.memberSignup(memberDto);
        return ResponseEntity.ok(200);
    }

    @PostMapping(path = path + "/login")
    public ResponseEntity memberLogin(@RequestBody  MemberDto.MemberLoginReqDto memberLoginReqDto) throws ParseException {
        return memberService.memberLogin(memberLoginReqDto);
    }
    //todo 이메일 인증 api 필요하고
    @PostMapping(path = path + "/sendEmail")
    public ResponseEntity memberSendEmail(@RequestBody  MemberEmailSendDto memberEmailSendDto) throws ParseException {
        return memberService.memberSendEmail(memberEmailSendDto);
    }
    //todo 주소 검색 api 프론트?

}