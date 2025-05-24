package com.shop.book.global.jwt;

import com.shop.book.domain.member.entity.Member;
import com.shop.book.global.config.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("check uri: " + path);

        // Pre-flight 요청은 필터를 타지 않도록 설정
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        // /api/member/로 시작하는 요청은 필터를 타지 않도록 설정
        if (path.startsWith("/api/member/")) {
            return true;
        }
        // todo book만들떄 url체크 수정필요 바뀌면
        if (path.startsWith("/api/book/view/")) {
            return true;
        }

        // Swagger UI 경로 제외 설정
        if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) {
            return true;
        }
        // h2-console 경로 제외 설정
        if (path.startsWith("/h2-console")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("------------------JWTCheckFilter.................");
        log.info("request.getServletPath(): {}", request.getServletPath());
        log.info("..................................................");


        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("AccessToken")) {
                if ("AccessToken".equals(cookie.getName())) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }
        // 2. 헤더에서도 읽기 (Bearer 토큰)
        if (authorization == null || authorization.isBlank()) {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                authorization = bearerToken.substring(7);
            }
        }
        //Authorization 헤더 검증
        if (authorization == null) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 email 획득
        Member member = jwtUtil.getMember(token);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customOAuth2User =  new CustomUserDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
