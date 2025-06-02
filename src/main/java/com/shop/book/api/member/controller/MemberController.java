package com.shop.book.api.member.controller;

import com.shop.book.api.member.service.MemberService;
import com.shop.book.domain.common.dto.ResponseDto;
import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.member.dto.MemberEmailSendDto;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import com.shop.book.global.jwt.JwtDto;
import com.shop.book.global.jwt.JwtUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping(path = path + "/signup")
    public ResponseEntity memberSignup(@RequestBody MemberDto memberDto){
        if (memberService.isEmailDuplicate(memberDto.getEmail())) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE);
        }
        memberService.memberSignup(memberDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", memberDto), HttpStatus.OK);
    }

    @PostMapping(path = path + "/signin")
    public ResponseEntity<?> memberLogin(@RequestBody  MemberDto.MemberLoginReqDto memberLoginReqDto) throws ParseException {
        JwtDto token = memberService.memberLogin(memberLoginReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 토큰 발급 성공", token), HttpStatus.OK);
    }
    @PostMapping(path = path + "/sendEmail")
    public ResponseEntity<?> memberSendEmail(@RequestBody  MemberEmailSendDto memberEmailSendDto) throws ParseException {
        return new ResponseEntity<>(new ResponseDto<>(1, "이메일 인증번호 생성 성공", memberService.memberSendEmail(memberEmailSendDto)), HttpStatus.OK);
    }


}