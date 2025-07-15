package ssu.cromi.teamit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateJwtToken(Authentication auth) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) auth.getPrincipal();
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        Key signingKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("nickName", userPrincipal.getNickName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    public boolean isValidToken(String Token){
       try{
           Jwts.parser()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJwt(Token);
           return true;
       }
       catch (JwtException e){
           return false;
       }
    }

    public String getUsernameFromJwt(String token){
        try {
            Jwt<Header, Claims> jwt = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJwt(token);
            return jwt.getBody().getSubject();
        }
        catch (JwtException e){
            return null;
        }
    }

    public String getNickNameFromJWT(String token){
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJwt(token)
                    .getBody();
            return body.get("nickName", String.class);
        }
        catch (JwtException e){
            return null;
        }
    }
}
