package org.example.service.token;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.token.RefreshToken;
import org.example.repository.token.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("unexpected token"));
    }

}
