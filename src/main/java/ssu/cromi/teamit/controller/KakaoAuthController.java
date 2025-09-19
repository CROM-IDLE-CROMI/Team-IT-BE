// package ssu.cromi.teamit.controller;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.util.UriComponentsBuilder;
// import ssu.cromi.teamit.DTO.auth.KakaoProfileCompleteRequest;
// import ssu.cromi.teamit.DTO.auth.KakaoProfileCompleteResponse;
// import ssu.cromi.teamit.DTO.auth.KakaoUserInfo;
// import ssu.cromi.teamit.domain.User;
// import ssu.cromi.teamit.security.JwtUtils;
// import ssu.cromi.teamit.service.UserService;

// import java.util.HashMap;
// import java.util.Map;

// @Slf4j
// @RestController
// @RequestMapping("/v1/auth/kakao")
// @RequiredArgsConstructor
// public class KakaoAuthController {

//     private final UserService userService;
//     private final JwtUtils jwtUtils;
//     private final RestTemplate restTemplate;

//     @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//     private String kakaoClientId;

//     @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
//     private String kakaoClientSecret;

//     @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//     private String kakaoRedirectUri;

//     @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
//     private String kakaoAuthUri;

//     @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
//     private String kakaoTokenUri;

//     @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
//     private String kakaoUserInfoUri;

//     /**
//      * 카카오 로그인 URL 생성
//      */
//     @GetMapping("/login-url")
//     public ResponseEntity<Map<String, String>> getKakaoLoginUrl() {
//         String loginUrl = UriComponentsBuilder.fromUriString(kakaoAuthUri)
//                 .queryParam("client_id", kakaoClientId)
//                 .queryParam("redirect_uri", kakaoRedirectUri)
//                 .queryParam("response_type", "code")
//                 .queryParam("scope", "profile_nickname,profile_image,account_email,birthday,birth_year")
//                 .build().toUriString();

//         Map<String, String> response = new HashMap<>();
//         response.put("loginUrl", loginUrl);

//         return ResponseEntity.ok(response);
//     }

//     /**
//      * 카카오 인증코드로 로그인 처리
//      */
//     @PostMapping("/login")
//     public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) {
//         try {
//             // 1. 인증코드로 액세스 토큰 받기
//             String accessToken = getKakaoAccessToken(code);

//             // 2. 액세스 토큰으로 사용자 정보 가져오기
//             KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

//             // 3. 사용자 정보 추출
//             Long kakaoId = kakaoUserInfo.getId();
//             String email = null;
//             String nickname = null;
//             String profileImage = null;
//             String birthday = null;
//             String birthyear = null;

//             // kakao_account에서 정보 추출
//             if (kakaoUserInfo.getKakaoAccount() != null) {
//                 email = kakaoUserInfo.getKakaoAccount().getEmail();
//                 birthday = kakaoUserInfo.getKakaoAccount().getBirthday();
//                 birthyear = kakaoUserInfo.getKakaoAccount().getBirthyear();
//             }

//             // properties에서 정보 추출
//             if (kakaoUserInfo.getProperties() != null) {
//                 nickname = kakaoUserInfo.getProperties().getNickname();
//                 profileImage = kakaoUserInfo.getProperties().getProfileImage();
//             }

//             // 4. 사용자 처리 (회원가입 또는 로그인)
//             User user = userService.processKakaoUser(kakaoId, email, nickname, profileImage);

//             // 5. JWT 토큰 생성
//             String jwtAccessToken = jwtUtils.generateJwtToken(user);
//             String refreshToken = jwtUtils.generateRefreshToken(user);

//             // 6. 프로필 완성도 체크
//             boolean isProfileComplete = userService.isProfileComplete(user.getUid());

//             // 7. 응답 생성
//             Map<String, Object> response = new HashMap<>();
//             response.put("accessToken", jwtAccessToken);
//             response.put("refreshToken", refreshToken);
//             response.put("isProfileComplete", isProfileComplete);
//             response.put("user", Map.of(
//                     "uid", user.getUid(),
//                     "nickname", user.getNickName(),
//                     "email", user.getEmail(),
//                     "profileImage", user.getProfileImg() != null ? user.getProfileImg() : ""
//             ));

//             return ResponseEntity.ok(response);

//         } catch (Exception e) {
//             log.error("Kakao login failed", e);
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("error", "kakao_login_failed");
//             errorResponse.put("message", e.getMessage());
//             return ResponseEntity.badRequest().body(errorResponse);
//         }
//     }

//     private String getKakaoAccessToken(String code) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Content-Type", "application/x-www-form-urlencoded");

//         String body = UriComponentsBuilder.newInstance()
//                 .queryParam("grant_type", "authorization_code")
//                 .queryParam("client_id", kakaoClientId)
//                 .queryParam("client_secret", kakaoClientSecret)
//                 .queryParam("redirect_uri", kakaoRedirectUri)
//                 .queryParam("code", code)
//                 .build()
//                 .getQuery();

//         HttpEntity<String> request = new HttpEntity<>(body, headers);
//         ResponseEntity<Map> response = restTemplate.exchange(kakaoTokenUri, HttpMethod.POST, request, Map.class);

//         @SuppressWarnings("unchecked")
//         Map<String, Object> responseBody = response.getBody();
//         return (String) responseBody.get("access_token");
//     }

//     private KakaoUserInfo getKakaoUserInfo(String accessToken) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "Bearer " + accessToken);
//         headers.set("Content-Type", "application/x-www-form-urlencoded");

//         HttpEntity<String> request = new HttpEntity<>(headers);
//         ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(kakaoUserInfoUri, HttpMethod.GET, request, KakaoUserInfo.class);

//         return response.getBody();
//     }

//     /**
//      * 카카오 사용자 프로필 완성 (추가 정보 입력)
//      */
//     @PostMapping("/complete-profile")
//     public ResponseEntity<KakaoProfileCompleteResponse> completeProfile(
//             HttpServletRequest request,
//             @Valid @RequestBody KakaoProfileCompleteRequest profileRequest) {
//         try {
//             // JWT에서 현재 사용자 UID 추출
//             String authHeader = request.getHeader("Authorization");
//             if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                 return ResponseEntity.badRequest().body(
//                         KakaoProfileCompleteResponse.builder()
//                                 .success(false)
//                                 .message("인증 토큰이 필요합니다.")
//                                 .build()
//                 );
//             }

//             String jwt = authHeader.substring(7);
//             String currentUid = jwtUtils.getUsernameFromJwt(jwt);

//             if (currentUid == null) {
//                 return ResponseEntity.badRequest().body(
//                         KakaoProfileCompleteResponse.builder()
//                                 .success(false)
//                                 .message("유효하지 않은 토큰입니다.")
//                                 .build()
//                 );
//             }

//             // 프로필 완성 처리
//             User updatedUser = userService.completeKakaoProfile(currentUid, profileRequest);

//             // 새로운 JWT 토큰 생성 (UID가 변경되었으므로)
//             String newAccessToken = jwtUtils.generateJwtToken(updatedUser);
//             String newRefreshToken = jwtUtils.generateRefreshToken(updatedUser);

//             // 응답 생성
//             KakaoProfileCompleteResponse.UserInfo userInfo = KakaoProfileCompleteResponse.UserInfo.builder()
//                     .uid(updatedUser.getUid())
//                     .nickname(updatedUser.getNickName())
//                     .email(updatedUser.getEmail())
//                     .profileImage(updatedUser.getProfileImg())
//                     .isProfileComplete(true)
//                     .build();

//             KakaoProfileCompleteResponse response = KakaoProfileCompleteResponse.builder()
//                     .success(true)
//                     .message("프로필이 성공적으로 완성되었습니다.")
//                     .user(userInfo)
//                     .build();

//             // 새로운 토큰을 헤더에 추가
//             HttpHeaders responseHeaders = new HttpHeaders();
//             responseHeaders.set("X-New-Access-Token", newAccessToken);
//             responseHeaders.set("X-New-Refresh-Token", newRefreshToken);

//             return ResponseEntity.ok().headers(responseHeaders).body(response);

//         } catch (Exception e) {
//             log.error("Profile completion failed", e);
//             return ResponseEntity.badRequest().body(
//                     KakaoProfileCompleteResponse.builder()
//                             .success(false)
//                             .message(e.getMessage())
//                             .build()
//             );
//         }
//     }
// }