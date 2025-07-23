package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.domain.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //토큰 값으로 조회
    Optional<RefreshToken> findByToken(String token);
    // 특정 유저의 토큰 모두 삭제
    void deleteByUser(User user);
}
