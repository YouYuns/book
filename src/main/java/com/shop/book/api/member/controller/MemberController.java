package com.shop.book.api.member.controller;

import com.shop.book.api.member.service.MemberService;
import com.shop.book.domain.member.dto.MemberDto;
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
        memberService.memberSignup(memberDto);
        return ResponseEntity.ok(200);
    }

    @PostMapping(path = path + "/login")
    public ResponseEntity memberLogin(@RequestBody  MemberDto.MemberLoginReqDto memberLoginReqDto) throws ParseException {
        memberService.memberLogin(memberLoginReqDto);
        return ResponseEntity.ok(200);
    }
}