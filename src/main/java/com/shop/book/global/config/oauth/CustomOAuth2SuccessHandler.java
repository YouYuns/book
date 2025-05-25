package com.shop.book.global.config.oauth;

import com.shop.book.domain.member.entity.Member;
import com.shop.book.global.config.security.CustomUserDetails;
import com.shop.book.global.jwt.JwtUtil;
import com.shop.book.global.jwt.JwtDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            //OAuth2User
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Member member = customUserDetails.getUser();

            //Jwt 토큰생성 Oauth2 로그인 성공시
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            System.out.println("OAuth2 jwt 토큰생성 Handler Role => " + role);

            String accessToken = null;
            String refreshToken = null;
            try {
                accessToken = jwtUtil.createAccessToken(member);
                refreshToken = jwtUtil.createRefreshToken(member);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

//            response.addCookie(createCookie("AccessToken", accessToken));
//            response.addCookie(createCookie("RefreshToken", refreshToken));

            //ROLE에 따른 redirect
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
           if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
            } else if (roles.contains("ROLE_SELLER")) {
                response.sendRedirect("/seller?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
            } else {
                response.sendRedirect("/?accessToken=" + accessToken + "&refreshToken=" + refreshToken); // 일반 사용자의 경우 메인 페이지로
            }
        }else {
            response.sendRedirect("/");
        }
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //todo https 환경에서만 가능하게 로컬환경은 http
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
