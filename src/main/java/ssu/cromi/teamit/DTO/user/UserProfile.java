package ssu.cromi.teamit.DTO.user;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode
@ToString
public class UserProfile {
    @NotBlank(message = "uid 누락됨")
    @Getter
    private String uid;

    @NotBlank(message = "닉네임 누락됨")
    @Getter
    private String nickName;

    @ToString.Exclude // pw는 toString, Equals 제외
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "PW 누락됨")
    @Getter
    @Length(min = 8, max=20, message = "최소길이 8, 최대길이 20")// pw 최소, 최대 길이 설정
    private String password;

    @NotBlank(message = "이메일 누락됨")
    @Getter
    @Email(message = "유효한 이메일 형식이 아님")
    private String email;

    @NotNull(message = "이메일 인증여부 누락됨")
    @Getter
    private boolean emailVerified;

    @NotNull(message = "생년월일 누락됨")
    @Getter
    private Integer birthday;

    @Getter
    @PositiveOrZero(message = "정의되지 않은 포인트")
    private Long point;

    @Getter
    private String profileImg;

    @Getter
    private Double myScore;

    @Getter
    @NotBlank(message = "유저 권한 누락됨")
    private String roles = "ROLE_USER";


}
