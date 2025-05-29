package com.shop.book.api.member.service.impl;

import com.shop.book.api.member.repository.MemberRepository;
import com.shop.book.api.member.service.MemberService;
import com.shop.book.domain.member.constant.Role;
import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.common.dto.ResponseDto;
import com.shop.book.domain.member.dto.MemberEmailSendDto;
import com.shop.book.domain.member.entity.Member;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import com.shop.book.global.jwt.JwtUtil;
import com.shop.book.global.jwt.JwtDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;
    @Override
    public void memberSignup(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity(passwordEncoder).addRole(Role.USER));
    }

    @Override
    public ResponseEntity<?> memberLogin(MemberDto.MemberLoginReqDto memberLoginReqDto) throws ParseException {
        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())){
            throw new BusinessException(ErrorCode.PAASSWORD_NOT_MATCH);
        }
        JwtDto token = jwtUtil.createTokens(member);
        MemberDto.MemberLoginResDto memberLoginResDto = new MemberDto.MemberLoginResDto(member);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", token), HttpStatus.OK);
    }

    public boolean isEmailDuplicate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public ResponseEntity memberSendEmail(MemberEmailSendDto memberEmailSendDto) {

        // todo create 랜덤 번호 생성
        return null;
    }


}
