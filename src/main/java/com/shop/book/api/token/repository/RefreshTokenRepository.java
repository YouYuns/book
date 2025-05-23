package com.shop.book.api.token.repository;

import com.shop.book.domain.common.RefreshToken;
import com.shop.book.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByEmail(String email);

    Optional<RefreshToken> findByRefreshToken(String token);
}
