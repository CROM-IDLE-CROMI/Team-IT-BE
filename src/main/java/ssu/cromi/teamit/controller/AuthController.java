package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.auth.JwtResponse;
import ssu.cromi.teamit.DTO.auth.LoginRequest;
import ssu.cromi.teamit.DTO.auth.SignupRequest;
import ssu.cromi.teamit.DTO.common.ApiResponse;
import ssu.cromi.teamit.DTO.common.UserResponse;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.service.AuthService;
import ssu.cromi.teamit.service.UserService;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authservice){
        this.userService = userService;
        this.authService = authservice;
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

}
