package com.shop.book.global.config.security;


import com.shop.book.api.repository.MemberRepository;
import com.shop.book.global.config.oauth.CustomOAuth2SuccessHandler;
import com.shop.book.global.config.oauth.CustomOAuth2UserService;
import com.shop.book.global.jwt.CustomAccessDeniedHandler;
import com.shop.book.global.jwt.CustomAuthenticationEntryPoint;
import com.shop.book.global.jwt.JwtFilter;
import com.shop.book.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final CorsConfigurationSource corsConfigurationSource;
    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                // 새로운 방식으로 CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                //csrf disable
                .csrf(AbstractHttpConfigurer::disable)

                //form 로그인 방식 disalbe
                .formLogin(AbstractHttpConfigurer::disable)

                //http basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)
                //JwtFilter 추가
                .addFilterBefore(new JwtFilter(jwtUtil, memberRepository), UsernamePasswordAuthenticationFilter.class)
                //JwtAuthentication Exception Hadling
                .exceptionHandling(authenticationManager -> authenticationManager
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))

                .authorizeHttpRequests(authorize -> authorize
                                // 공개 접근 허용 URL 설정
                                .requestMatchers("/" ).permitAll()
                                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/seller/**").hasRole("SELLER")
                                .requestMatchers("/mypage/**").hasRole("USER")
                                .anyRequest().authenticated()
                )

                //JWT 사용하기 때문에 세션 설정 : STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                //GET 요청을 통해 로그아웃을 처리하도록 허용
                .logout(logout -> logout.logoutRequestMatcher(
                        new OrRequestMatcher(
                                new AntPathRequestMatcher("/logout", "GET"),
                                new AntPathRequestMatcher("/logout", "POST")
                        )
                ))
//                .userDetailsService(customUserDetailsService)
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            // OAuth2 로그인 실패 시 처리
                            response.sendRedirect("/login?error=oauth2");
                        })
                );

        return http.build();
    }
}
