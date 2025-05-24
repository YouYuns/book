package com.shop.book.global.config.security;

import com.shop.book.api.member.repository.MemberRepository;
import com.shop.book.domain.member.constant.OAuthType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 사용자 DB에서 USER 체크
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(memberRepository.findByEmailAndOauthType(email, OAuthType.NONE)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found USER")));
        //Set<Role>을 GrantedAuthority로바꿔줘야된다.
        //Collection<? extends GrantedAuthority> authorities
    }
}
