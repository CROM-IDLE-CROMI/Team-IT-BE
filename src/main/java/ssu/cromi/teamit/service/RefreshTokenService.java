package ssu.cromi.teamit.service;

import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.exceptions.TokenRefreshException;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * 지정된 유저용 Refresh token을 생성 및 지정함
     * @param user 토큰을 발급할 User 엔티티
     * @return 저장된 RefreshToken 엔티티
     */
    RefreshToken createRefreshToken(User user);

    /**
     * 토큰 만료 여부 검사, 만료 시 삭제 후 예외처리
     * @param token 검증할 RefreshToken 엔티티
     * @return 만료되지 않은 토큰
     * @throws TokenRefreshException 만료된 경우
     */
    RefreshToken verifyExpiration(RefreshToken token);

    /**
     * 유저가 보유한 모든 리프레시 토큰 삭제
     * @param user 삭제 대상 User엔티티
     */
    void deleteByUser(User user);

    Optional<RefreshToken> findByToken(String token);
}
