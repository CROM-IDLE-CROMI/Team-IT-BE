package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.dto.auth.JwtResponse;
import ssu.cromi.teamit.dto.auth.LoginRequest;
import ssu.cromi.teamit.dto.auth.RefreshTokenRequest;
import ssu.cromi.teamit.dto.auth.SignupRequest;
import ssu.cromi.teamit.dto.common.ApiResponse;
import ssu.cromi.teamit.dto.common.UserResponse;
import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.exceptions.TokenRefreshException;
import ssu.cromi.teamit.security.JwtUtils;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.AuthService;
import ssu.cromi.teamit.service.UserService;
import ssu.cromi.teamit.service.RefreshTokenService;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    @Autowired @Lazy
    private RefreshTokenService refreshTokenService;


    public AuthController(UserService userService, AuthService authservice, JwtUtils jwtUtils){
        this.userService = userService;
        this.authService = authservice;
        this.jwtUtils = jwtUtils;
    }
    //회원가입
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> signup(@RequestBody @Valid SignupRequest dto){
        User newUser = userService.register(dto);
        String createdAtStr = newUser.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_INSTANT);
        UserResponse data = new UserResponse(
                newUser.getUid(),
                newUser.getEmail(),
                newUser.getNickName(),
                createdAtStr,
                newUser.getBirthday()
        );
        return ApiResponse.success(data, "201", "success");
    }

    //로그인
    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@RequestBody @Valid LoginRequest dto){
        JwtResponse token = authService.authenticateAndCreateToken(dto);
        return ApiResponse.success(token, "200", "success");
    }

    //Jwt 재발급
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@Valid @RequestBody RefreshTokenRequest req){
        RefreshToken stored = refreshTokenService.findByToken(req.getRefreshToken())
                .orElseThrow(() -> new TokenRefreshException(req.getRefreshToken(), "DB에 존재하지 않음"));
        refreshTokenService.verifyExpiration(stored);

        //DB에서 얻은 사용자로 새 AccessToken 생성
        String newAccess = jwtUtils.generateJwtToken(stored.getUser());
        RefreshToken newRefresh = refreshTokenService.createRefreshToken(stored.getUser());

        JwtResponse res = new JwtResponse(newAccess, "Bearer", jwtUtils.getJwtExpirationMs(),newRefresh.getToken(),stored.getUser().getUid());
        return ResponseEntity.ok(res);
    }
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        refreshTokenService.deleteByUser(user);
    }
}
