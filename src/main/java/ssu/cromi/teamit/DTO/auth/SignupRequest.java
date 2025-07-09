package ssu.cromi.teamit.DTO.auth;
//회원가입 DTO
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDateTime;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SignupRequest {
    @NotBlank(message = "UID 누락됨")
    @Getter
    private final String uid;

    @NotBlank(message = "닉네임 누락됨")
    @Getter
    private final String nickName;

    @ToString.Exclude // pw는 toString, Equals 제외
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "PW 누락됨")
    @Getter
    @Length(min = 8, max=20, message = "최소길이 8, 최대길이 20")// pw 최소, 최대 길이 설정
    private final String password;

    @NotBlank(message = "이메일 누락됨")
    @Getter
    @Email(message = "유효한 이메일 형식이 아님")
    private final String email;

    @NotNull(message = "이메일 인증여부 누락됨")
    @Getter
    private final boolean emailVerified;

    @NotNull(message = "생년월일 누락됨")
    @Size(min = 19000101,max = 20250101, message = "유효하지 않은 생년월일")// 생년월일 검증하기
    @Getter
    private final Integer birthday;

    @NotNull
    @JsonFormat(
            shape    = JsonFormat.Shape.STRING,
            pattern  = "yyyy-MM-dd'T'HH:mm:ss",
            timezone = "Asia/Seoul"
    )
    private LocalDateTime createdAt;

    @NotNull
    @JsonFormat(
            shape    = JsonFormat.Shape.STRING,
            pattern  = "yyyy-MM-dd'T'HH:mm:ss",
            timezone = "Asia/Seoul"
    )
    private LocalDateTime updatedAt;

}
