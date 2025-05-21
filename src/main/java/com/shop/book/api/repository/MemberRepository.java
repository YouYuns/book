package com.shop.book.api.repository;

import com.shop.book.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmailAndIsSocial(String email, boolean isSocial);

    Optional<Member> findByEmail(String email);
}