package com.shop.book.api.token.repository;

import com.shop.book.domain.common.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByRefreshToken(String token);

    Optional<RefreshToken> findByMemberEmail(String email);
}
