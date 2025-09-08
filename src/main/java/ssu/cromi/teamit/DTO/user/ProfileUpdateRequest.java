package ssu.cromi.teamit.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class ProfileUpdateRequest {

    @NotBlank(message = "닉네임은 필수입니다")
    @Size(max = 100, message = "닉네임은 100자 이하여야 합니다")
    @Getter
    @Setter
    private String nickName;

    @NotNull(message = "생년월일은 필수입니다")
    @Getter
    @Setter
    private Integer birthday;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다")
    @Getter
    @Setter
    private String email;

    @Size(max = 500, message = "한줄소개는 500자 이하여야 합니다")
    @Getter
    @Setter
    private String introduction;

    @Getter
    @Setter
    private String position;
}