package com.teresol.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teresol.demo.entity.RefreshToken;
import com.teresol.demo.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash); // for refresh

    List<RefreshToken> findByUserAndRevokedAtIsNull(User user); // for logout/session management
}