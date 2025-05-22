package com.shop.book.global.config;


import com.shop.book.global.config.oauth.CustomOAuth2UserService;
import com.shop.book.global.jwt.service.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService){
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    @Bean
    public TokenManager tokenManager(){
        return new TokenManager(accessTokenExpirationTime, accessTokenExpirationTime, tokenSecret);
    }

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                //csrf disable
                .csrf(AbstractHttpConfigurer::disable)

                //form 로그인 방식 disalbe
                .formLogin(AbstractHttpConfigurer::disable)

                //http basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)

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
//                        .successHandler(customLoginSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            // OAuth2 로그인 실패 시 처리
                            response.sendRedirect("/login?error=oauth2");
                        })
                );

        return http.build();
    }
}
