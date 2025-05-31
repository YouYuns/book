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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final JavaMailSender javaMailSender;

    private static final String senderEmail = "tjdgh0312@gmail.com";

    private static final Map<String, String> verificationCodes = new HashMap<>();
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


    /**
     * 이메일 전송
     */
    @Override
    public MimeMessage memberSendEmail(MemberEmailSendDto memberEmailSendDto){
        authRandomCode(memberEmailSendDto.getEmaill());
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(memberEmailSendDto.getEmaill());
            helper.setSubject("이메일 인증번호");
            String body = "<h2>Book Shop에 오신걸 환영합니다!</h2><h3>아래의 인증번호를 입력하세요.</h3><h1>" + verificationCodes.get(memberEmailSendDto.getEmaill()) + "</h1><h3>감사합니다.</h3>";
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }
    //todo 인증번호 확인 Redis 코드로 바꿀예정
    // 6자리 랜덤코드 생성
    public static String authRandomCode(String email) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 6; i++) {
            if(Math.random() < 0.5) {
                sb.append( (char)((int)(Math.random() * 10) + '0') );
            } else {
                sb.append( (char)((int)(Math.random() * 26) + 'A') );
            }
        }
        return verificationCodes.put(email, sb.toString());
    }
}
