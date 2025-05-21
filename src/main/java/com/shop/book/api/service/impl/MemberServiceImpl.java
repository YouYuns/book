package com.shop.book.api.service.impl;

import com.shop.book.api.repository.MemberRepository;
import com.shop.book.api.service.MemberService;
import com.shop.book.domain.member.constant.Role;
import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.member.dto.ResponseDto;
import com.shop.book.domain.member.entity.Member;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void memberSignup(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity(passwordEncoder).addRole(Role.USER));
    }

    @Override
    public ResponseEntity<?> memberLogin(MemberDto.MemberLoginReqDto memberLoginReqDto) {
        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())){
            throw new BusinessException(ErrorCode.PAASSWORD_NOT_MATCH);
        }
        MemberDto.MemberLoginResDto memberLoginResDto = new MemberDto.MemberLoginResDto(member);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", memberLoginResDto), HttpStatus.OK);
    }
}
