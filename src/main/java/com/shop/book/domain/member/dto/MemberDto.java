package com.shop.book.domain.member.dto;


import com.shop.book.domain.member.constant.OAuthType;
import com.shop.book.domain.member.constant.Role;
import com.shop.book.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class MemberDto {
    private String email;
    private String password;
    private String memberName;
    private String status;
    private OAuthType oAuthType;
    private Set<Role> roles;


    @Getter
    public static class MemberLoginReqDto {
        private String email;
        private String password;
    }

    @Getter
    public static class MemberLoginResDto{
        private String email;
        private String memberName;

        public MemberLoginResDto(Member member){
            this.email = member.getEmail();
            this.memberName = member.getMemberName();
        }
    }


    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberName(memberName)
                .oAuthType(oAuthType)
                .status(status)
                .roles(roles)
                .build();
    }
}
