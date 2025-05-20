package com.shop.book.domain.member.entity;


import com.shop.book.domain.common.BaseEntity;
import com.shop.book.domain.member.constant.MemberType;
import com.shop.book.domain.member.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter 
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 생성자를 막기위해 , @Setter안쓰는것과 같음
public class Member extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberType memberType;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(length = 200)
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;


    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;


}
