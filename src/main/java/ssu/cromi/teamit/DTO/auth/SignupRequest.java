package ssu.cromi.teamit.DTO.auth;
//회원가입 DTO
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SignupRequest {
    @JsonProperty("uid")
    @NotBlank(message = "UID 누락됨")
    private String uid;

    @JsonProperty("nickName")
    @NotBlank(message = "닉네임 누락됨")
    private String nickName;

    @JsonProperty("password")
    @ToString.Exclude // pw는 toString, Equals 제외
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "PW 누락됨")
    @Length(min = 8, max=20, message = "최소길이 8, 최대길이 20")// pw 최소, 최대 길이 설정
    private String password;

    @JsonProperty("email")
    @NotBlank(message = "이메일 누락됨")
    @Email(message = "유효한 이메일 형식이 아님")
    private String email;

    @JsonProperty("emailVerified")
    private boolean emailVerified;

    @JsonProperty("birthDay")
    private Integer birthDay;
}
