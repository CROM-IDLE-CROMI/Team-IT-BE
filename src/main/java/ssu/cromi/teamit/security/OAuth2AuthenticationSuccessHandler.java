package ssu.cromi.teamit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.service.UserService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${FRONTEND_URI:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        try {
            // 카카오 사용자 정보 처리 및 JWT 토큰 생성
            User user = processOAuth2User(oauth2User);
            String accessToken = jwtUtils.generateJwtToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            // 프론트엔드로 리디렉션 (토큰과 함께)
            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/callback")
                    .queryParam("access_token", URLEncoder.encode(accessToken, StandardCharsets.UTF_8))
                    .queryParam("refresh_token", URLEncoder.encode(refreshToken, StandardCharsets.UTF_8))
                    .queryParam("user_id", URLEncoder.encode(user.getUid(), StandardCharsets.UTF_8))
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 authentication success handling failed", e);

            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/error")
                    .queryParam("error", URLEncoder.encode("authentication_failed", StandardCharsets.UTF_8))
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }

    private User processOAuth2User(OAuth2User oauth2User) {
        // OAuth2User에서 카카오 사용자 정보 추출
        Long kakaoId = oauth2User.getAttribute("id");
        String email = null;
        String nickname = null;
        String profileImage = null;

        // kakao_account에서 정보 추출
        if (oauth2User.getAttributes().get("kakao_account") != null) {
            @SuppressWarnings("unchecked")
            var kakaoAccount = (java.util.Map<String, Object>) oauth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
        }

        // properties에서 정보 추출
        if (oauth2User.getAttributes().get("properties") != null) {
            @SuppressWarnings("unchecked")
            var properties = (java.util.Map<String, Object>) oauth2User.getAttributes().get("properties");
            nickname = (String) properties.get("nickname");
            profileImage = (String) properties.get("profile_image");
        }

        // UserService를 통해 사용자 처리 (회원가입 또는 로그인)
        return userService.processKakaoUser(kakaoId, email, nickname, profileImage);
    }
}