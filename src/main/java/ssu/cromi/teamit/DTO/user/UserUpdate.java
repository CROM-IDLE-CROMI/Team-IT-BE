package ssu.cromi.teamit.DTO.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserUpdate {

    @NotBlank(message = "닉네임 누락됨")
    @Getter
    @Setter
    private String nickName;

    @NotNull(message = "업데이트 날짜 누락됨")
    @Getter
    @Setter
    @JsonFormat(
            shape    = JsonFormat.Shape.STRING,
            pattern  = "yyyy-MM-dd'T'HH:mm:ss",
            timezone = "Asia/Seoul"
    )
    private LocalDateTime updatedAt;

    @NotNull(message = "생년월일 누락됨")
    @Getter
    @Setter
    private Integer birthday;

    @Getter
    @Setter
    private String profileImg;
}