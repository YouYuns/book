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
import com.shop.book.global.util.RedisUtil;
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
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final JavaMailSender javaMailSender;

    private final RedisUtil redisUtil;


    private int authNumber;
    @Override
    public void memberSignup(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity(passwordEncoder).addRole(Role.USER));
    }

    @Override
    public JwtDto memberLogin(MemberDto.MemberLoginReqDto memberLoginReqDto) throws ParseException {
        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())){
            throw new BusinessException(ErrorCode.PAASSWORD_NOT_MATCH);
        }
        JwtDto token = jwtUtil.createTokens(member);
        return token;
    }

    public boolean isEmailDuplicate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }


    /**
     * 이메일 전송
     */
    @Override
    public String memberSendEmail(MemberEmailSendDto memberEmailSendDto){
        makeRandomNumber();
        String setFrom = "tjdgh0312@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = memberEmailSendDto.getEmaill();
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content = "<h2>Book Shop에 오신걸 환영합니다!</h2><h3>아래의 인증번호를 입력하세요.</h3><h1>" + authNumber + "</h1><h3>감사합니다.</h3>";
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    //임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        this.authNumber = Integer.parseInt(randomNumber);
    }


    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            javaMailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*5L);
    }
    public boolean checkAuthNum(String email,String authNum){
        if(redisUtil.getData(authNum)==null){
            return false;
        }
        else if(redisUtil.getData(authNum).equals(email)){
            return true;
        }
        else{
            return false;
        }
    }
}
