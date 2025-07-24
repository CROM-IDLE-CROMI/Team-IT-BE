package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.auth.JwtResponse;
import ssu.cromi.teamit.DTO.auth.LoginRequest;
import ssu.cromi.teamit.DTO.auth.RefreshTokenRequest;
import ssu.cromi.teamit.DTO.auth.SignupRequest;
import ssu.cromi.teamit.DTO.common.ApiResponse;
import ssu.cromi.teamit.DTO.common.UserResponse;
import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.exceptions.TokenRefreshException;
import ssu.cromi.teamit.security.JwtUtils;
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
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, AuthService authservice, RefreshTokenService refreshTokenService, JwtUtils jwtUtils){
        this.userService = userService;
        this.authService = authservice;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
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
    public ResponseEntity<JwtResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request){
        RefreshToken stored = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(()-> new TokenRefreshException(request.getRefreshToken(), "RefreshToken이 DB에 존재하지 않음"));
        refreshTokenService.verifyExpiration(stored);
        String newAccess = jwtUtils.refreshAccessToken(request.getRefreshToken());
        RefreshToken newRefresh = refreshTokenService.createRefreshToken(stored.getUser());
        JwtResponse response = new JwtResponse(
                newAccess,
                jwtUtils.getJwtExpirationMs(),
                newRefresh.getToken()
        );
        return ResponseEntity.ok(response);
    }
}
