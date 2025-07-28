package ssu.cromi.teamit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.exceptions.TokenRefreshException;
import ssu.cromi.teamit.service.RefreshTokenService;
import ssu.cromi.teamit.service.impl.UserDetailsServiceImpl;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, RefreshTokenService refreshTokenService, UserDetailsServiceImpl userDetailsService){
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        try{
            String accessToken = parseJwt(request);
            // 1) Access Token 유효한 경우
            if (accessToken != null && jwtUtils.isValidToken(accessToken)) {
                authenticateWithToken(accessToken, request);
            }
            // 2) 만료된 경우 → Refresh Token으로 재발행
            else if(accessToken != null && jwtUtils.isTokenExpired(accessToken)) {
                String refreshToken = parseRefreshToken(request);
                if (refreshToken != null) {
                    RefreshToken stored = refreshTokenService.findByToken(refreshToken)
                            .orElseThrow(() -> new TokenRefreshException(
                                    refreshToken, "RefreshToken이 없습니다"));
                    refreshTokenService.verifyExpiration(stored);
                    // 새 Access Token 발행
                    String newAccess = jwtUtils.refreshAccessToken(refreshToken);

                    // 응답 헤더에 새 토큰 세팅
                    response.setHeader("Authorization", "Bearer " + newAccess);

                    // 새 토큰으로 인증 컨텍스트 설정
                    authenticateWithToken(newAccess, request);
                }
            }
        }
        catch (Exception e){
            logger.error("AuthTokenFilter Error : 사용자 인증 설정 실패", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    /** 인증 컨텍스트 설정 분리 메서드 */
    private void authenticateWithToken(String token, HttpServletRequest request) {
        String uid = jwtUtils.getUsernameFromJwt(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(uid);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    /** Refresh Token 추출 */
    private String parseRefreshToken(HttpServletRequest request) {
        String header = request.getHeader("Refresh-Token");
        return StringUtils.hasText(header) ? header : null;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return "/v1/auth/refresh".equals(request.getServletPath());
    }
}