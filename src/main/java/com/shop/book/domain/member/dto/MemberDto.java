package com.shop.book.domain.member.dto;


import com.shop.book.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class MemberDto {
    private String email;
    private String password;
    private String nickName;
    private String memberName;


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
                .build();
    }
}
