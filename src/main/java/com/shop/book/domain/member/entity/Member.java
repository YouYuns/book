package com.shop.book.domain.member.entity;


import com.shop.book.domain.common.BaseEntity;
import com.shop.book.domain.member.constant.MemberStatus;
import com.shop.book.domain.member.constant.OAuthType;
import com.shop.book.domain.member.constant.Role;
import com.shop.book.domain.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@SequenceGenerator(name = "GENERATOR_MEMBER",
        sequenceName = "SEQ_MEMBER", initialValue = 1001, allocationSize = 1)
@Table(name ="MEMBER")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 생성자를 막기위해 , @Setter안쓰는것과 같음
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {


    //사용자 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_MEMBER")
    private Long id;

    //OAuth타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OAuthType oAuthType;

    //이메일 -> 로그인 ID로 사용
    @Column(unique = true, length = 50, nullable = false)
    private String email;

    //비밀번호
    @Column(length = 200)
    private String password;


    // 핸드폰번호
    @Column(nullable = false)
    private String phoneNumber;

    // 성별
    @Column(nullable = false)
    private String gender;

    //사용자이름
    @Column(nullable = false, length = 20)
    private String memberName;

    // 생년월일
    @Column(nullable = false)
    private String birthDate;

    // 우편번호
    @Column(nullable = false)
    private String postcode;

    // 주소
    @Column(nullable = false)
    private String address;

    // 공동현관 비밀번호
    @Column(nullable = true)
    private String entrance;

    // 참고항목
    @Column(nullable = false)
    private String extraAddress;

    // 상세주소
    @Column(nullable = false)
    private String detailAddress;

    //사용자사진
    @Column(length = 200)
    private String profile;

    // 회원상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private String status;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "userId"))
    @Builder.Default
    @Column(name = "role")
    private Set<Role> roles = new HashSet<Role>();


    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;


    public Member addRole(Role role){
        roles.add(role);
        return this;
    }


    public MemberDto toDto(){
        return MemberDto.builder()
                .email(email)
                .password(password)
                .memberName(memberName)
                .oAuthType(oAuthType)
                .status(status)
                .roles(roles)
                .build();
    }
}
