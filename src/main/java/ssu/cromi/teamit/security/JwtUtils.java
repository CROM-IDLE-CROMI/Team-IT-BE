package ssu.cromi.teamit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.repository.UserRepository;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration-access}")
    @Getter
    private long jwtExpirationMs;
    @Value("${jwt.expiration-refresh}")
    @Getter
    private long refreshExpirationMs;

    @Autowired
    private UserRepository userRepository;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateJwtToken(User user) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        Key signingKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(user.getUid())
                .claim("nickName", user.getNickName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getUid())
                .issuedAt(new Date())
                // refresh 만료시간
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isValidToken(String Token){
        try{
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(Token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }
    /**
     * JWT에서 subject(uid)를 읽는 메서드.
     * @param token 검사할 JWT 문자열
     * @return 토큰이 유효하면 subject(uid), 아니면 null
     */
    public String getUsernameFromJwt(String token){
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return jws.getPayload().getSubject();
        }
        catch (JwtException | IllegalArgumentException e){
            return null;
        }
    }
    /**
     * JWT에서 "nickName" 클레임을 꺼내오는 메서드.
     * @param token 검사할 JWT 문자열
     * @return 토큰이 유효하면 nickName 값, 아니면 null
     */
    public String getNickNameFromJWT(String token){
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return jws.getPayload().get("nickName", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String refreshAccessToken(String refreshToken){
        try{
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(refreshToken);
            String uid = jws.getPayload().getSubject();
            User user = userRepository.findByUid(uid)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: "+ uid));
            return generateJwtToken(user);
        }
        catch (ExpiredJwtException e){
            throw e;
        }
        catch (JwtException | IllegalArgumentException e){
            throw new JwtException("Invalid refresh token", e);
        }
    }
}