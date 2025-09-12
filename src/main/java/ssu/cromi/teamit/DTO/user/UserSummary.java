package ssu.cromi.teamit.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class UserSummary {
    @NotBlank(message = "uid누락됨")
    private final String uid;
    @NotBlank(message = "닉네임 누락됨")
    private final String nickname;
    @NotBlank(message = "email 누락됨")
    @Email
    private final String email;
    @NotNull(message = "이메일인증 누락됨")
    private final boolean emailVerified;
    @NotNull(message = "생성시각 누락됨")
    private final LocalDateTime createdAt;
    @NotNull(message = "최종수정시각 누락됨")
    private final LocalDateTime updatedAt;
}