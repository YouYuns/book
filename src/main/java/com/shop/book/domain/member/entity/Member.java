package com.shop.book.domain.member.entity;


import com.shop.book.domain.common.BaseEntity;
import com.shop.book.domain.member.constant.MemberType;
import com.shop.book.domain.member.constant.Role;
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
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 생성자를 막기위해 , @Setter안쓰는것과 같음
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_MEMBER")
    private Long id;

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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ROLE")
    @ElementCollection(fetch = FetchType.EAGER)
    // DEFAULT는 LAZY이여서 지연로딩을 하면 안된다. 회원정보를 읽을때 바로바로 읽어야 된다.
    private Set<Role> roleSet = new HashSet<>();


    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;


    public Member addRole(Role role){
        roleSet.add(role);
        return this;
    }
}
