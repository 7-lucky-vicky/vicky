package com.sparta.vicky.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    @Transactional
    public void save(String username, String refreshToken) {
        Optional<RefreshToken> existToken = findByUsername(username);

        if (existToken.isPresent()) {
            existToken.get().update(refreshToken);
        } else {
            refreshTokenRepository.save(new RefreshToken(username, refreshToken));
        }
    }

}
