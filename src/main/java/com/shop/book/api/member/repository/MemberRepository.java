package com.shop.book.api.member.repository;

import com.shop.book.domain.member.constant.OAuthType;
import com.shop.book.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmailAndOauthType(String email, OAuthType oAuthType);

    Optional<Member> findByEmail(String email);
}