package com.shop.book.global.config.security;

import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails extends User implements OAuth2User {

    private static final long serialVersionUID = 1L;
    private String email;
    private String name;
    private Member member; // Member 참조 추가
    private Long userId;
    private String password;
    private String status;
    private String phoneNumber;
    private String birthDate;
    private String gender;

    public CustomUserDetails(Member member) {

        // username, password, auth
        super(member.getEmail(), member.getPassword(),
                member.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet()));

        this.email = member.getEmail();
        this.name = member.getMemberName();
        this.member = member; // MemberEntity 저장
        this.userId = member.getId();
        this.password = member.getPassword();
        this.status = member.getStatus();
        this.phoneNumber = member.getPhoneNumber();
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender();
    }


    // Member 반환 메소드 추가
    public Member getUser() {
        return this.member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}
