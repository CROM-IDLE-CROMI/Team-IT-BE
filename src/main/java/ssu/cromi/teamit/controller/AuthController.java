package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.auth.SignupRequest;
import ssu.cromi.teamit.DTO.common.ApiResponse;
import ssu.cromi.teamit.DTO.common.UserResponse;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.service.UserService;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> signup(
            @RequestBody @Valid SignupRequest dto
    ){
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
}
