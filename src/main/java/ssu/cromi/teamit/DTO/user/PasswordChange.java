package ssu.cromi.teamit.DTO.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
public class PasswordChange {
    @Getter
    @NotBlank(message = "기존 비밀번호 누락")
    @Setter
    private String currentPassword;

    @Setter
    @Getter
    @Length(min = 8, max=20, message = "최소길이 8, 최대길이 20")// pw 최소, 최대 길이 설정
    @NotBlank(message = "신규 비밀번호 누락")
    private String newPassword;
}