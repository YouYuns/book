package com.shop.book.global.jwt;


import com.shop.book.api.member.repository.MemberRepository;
import com.shop.book.api.token.repository.RefreshTokenRepository;
import com.shop.book.domain.common.domain.RefreshToken;
import com.shop.book.domain.member.entity.Member;
import com.shop.book.global.config.security.CustomUserDetailsService;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey ;
    private final Long expirationTime;
    private final Long refreshExpirationTime;

    private final CustomUserDetailsService customUserDetailsService;

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;


    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.expiration-time}") Long expirationTime,
            @Value("${spring.jwt.refresh-expiration-time}") Long refreshExpirationTime,
            CustomUserDetailsService customUserDetailsService, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expirationTime = expirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    // 토큰에서 회원 이름 추출
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    // 토큰에서 이메일 추출 PK
    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    //JWT 토큰에서 인증정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Member getMember(String token) {
        return memberRepository.findByEmail(this.getEmail(token))
                .orElseThrow(() -> new UsernameNotFoundException("Not Found USER"));
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    public JwtDto createTokens(Member member) throws ParseException {
        String accessToken = createAccessToken(member);
        String refreshToken = createRefreshToken(member);

        log.info("success to create tokens");
        return JwtDto.builder()
                .grantType("Bearer")
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
    }

    public String createAccessToken(Member member) throws ParseException {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        // JWT access token 생성
        return Jwts.builder()
                .claim("email", member.getEmail())
                .claim("name", member.getMemberName())
                .claim("roles", member.getRoles())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();// builder에서 최종적으로 JWT string 생성
    }

    public String createRefreshToken(Member member) throws ParseException {

        // 토큰의 subject가 user의 Email로 저장되므로 loadUserByUsername()의 인자로 넘기기
//        Member member = (Member) customUserDetailsService.loadUserByUsername(accessToken);
        // 해당 유저에 대해 refresh token 조회
        Optional<RefreshToken> refreshTokenByUser = refreshTokenRepository.findByMemberEmail(member.getEmail());

        // refresh token이 이미 존재하는 경우
        if(refreshTokenByUser.isPresent())
            return refreshTokenByUser.get().getRefreshToken(); // 해당 refresh token 반환

        // refresh token이 존재하지 않으므로 refresh token 생성
        Date refreshValidity = new Date(new Date().getTime() + refreshExpirationTime); // refreshToken 만료 기간 설정
        String refreshToken = Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(refreshValidity)
                .signWith(secretKey)
                .compact(); // 최종 JWT string 생성

        // RefreshTokenRepository에 user와 매핑해 refresh token 저장
        refreshTokenRepository.save(new RefreshToken(member, refreshToken, refreshValidity));

        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("JWT token not supported.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token is invalid");
        }
        return false;
    }

    public String validateRefreshToken(String accessToken, String token) throws ParseException {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_INVALD));

        // 만료된 accessToken의 주인과 입력한 refresh token의 유저가 같은지 판단
        if(!this.getEmail(accessToken).equals(refreshToken.getMember().getEmail()))
            throw new BusinessException(ErrorCode.NOT_MATCH_USER_ACCESSTOKEN_REFRESHTOKEN);

        // refresh token이 유효하지 않은 경우 (만료기간이 지난 경우) => 로그인 풀도록 요청
        if(refreshToken.getValidityDate().before(new Date())) {
            // RefreshToken DB에서 해당 엔티티 삭제 후 만료 response 반환
            refreshTokenRepository.delete(refreshToken);
            return HttpStatus.UNAUTHORIZED.toString();
        }
        // refresh token이 유효한 경우 => access token 발급
        else{
            Member member = (Member) customUserDetailsService.loadUserByUsername(refreshToken.getMember().getEmail());
            return createAccessToken(member);
        }
    }
}
