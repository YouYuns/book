package com.shop.book.domain.common;

import com.shop.book.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
//ORACLE
//@SequenceGenerator(
//        name = "GENERATOR_REFRESH_TOKEN",
//        sequenceName = "SEQ_REFRESH_TOKEN",
//        initialValue = 1,
//        allocationSize = 1
//)
public class RefreshToken {

    @Id
    //ORACLE
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_REFRESH_TOKEN")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
    private String refreshToken;
    private Date validityDate;

    @Builder
    public RefreshToken(Member member, String refreshToken, Date validityDate) {
        this.member = member;
        this.refreshToken = refreshToken;
        this.validityDate = validityDate;
    }
}
