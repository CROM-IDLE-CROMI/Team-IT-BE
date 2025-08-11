package ssu.cromi.teamit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    @NotBlank(message = "refreshToken 누락됨")
    private String refreshToken;
}
