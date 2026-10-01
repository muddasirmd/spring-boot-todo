package com.teresol.demo.security;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


import com.teresol.demo.entity.RefreshToken;
import com.teresol.demo.entity.User;
import com.teresol.demo.exception.InvalidRefreshTokenException;
import com.teresol.demo.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public String create(User user) {

        String rawToken = generateRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hashRefreshToken(rawToken))
                .expiresAt(
                    Instant.now().plus(
                        Duration.ofDays(30)
                    )
                )
                .createdAt(Instant.now())
                .build();

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    public RefreshToken validate(String rawToken) {

        String hash = hashRefreshToken(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenHash(hash)
                        .orElseThrow(() ->
                            new InvalidRefreshTokenException()
                        );

        if (refreshToken.getRevokedAt() != null) {
            throw new InvalidRefreshTokenException();
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException();
        }

        return refreshToken;
    }
    

    // TODO: Learn About this
    private String generateRefreshToken() {

        byte[] randomBytes = new byte[64];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashRefreshToken(String token) {

        return DigestUtils.sha256Hex(token); // TODO: Learn About this
    }
}