package ssu.cromi.teamit.DTO.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "로그인 uid 입력 안됨")
    @Getter
    private final String uid;

    @NotBlank(message = "로그인 pw 입력 안됨")
    @Getter
    @ToString.Exclude // pw는 toString, Equals 제외
    @EqualsAndHashCode.Exclude
    private final String password;
}