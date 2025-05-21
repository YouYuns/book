package com.shop.book.api.controller;

import com.shop.book.api.service.MemberService;
import com.shop.book.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    @PostMapping("/signup")
    public ResponseEntity memberSignup(@RequestBody MemberDto memberDto){
        memberService.memberSignup(memberDto);
        return ResponseEntity.ok(200);
    }

    @PostMapping("/login")
    public ResponseEntity memberLogin(MemberDto.MemberLoginReqDto memberLoginReqDto){
        memberService.memberLogin(memberLoginReqDto);
        return ResponseEntity.ok(200);
    }
}