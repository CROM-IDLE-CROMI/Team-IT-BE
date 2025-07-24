package ssu.cromi.teamit.service.impl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.exceptions.TokenRefreshException;
import ssu.cromi.teamit.repository.RefreshTokenRepository;
import ssu.cromi.teamit.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenService refreshTokenService;
    @Value("${jwt.expiration-refresh}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService){
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public RefreshToken createRefreshToken(User user){
        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }
    @Override
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please sign in again.");
        }
        return token;
    }

    @Override
    public void deleteByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
}
