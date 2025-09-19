package ssu.cromi.teamit.DTO.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoProfileCompleteRequest {

    @JsonProperty("customUid")
    @NotBlank(message = "사용자 정의 ID는 필수입니다")
    @Size(min = 4, max = 20, message = "사용자 ID는 4~20자여야 합니다")
    private String customUid;

    @JsonProperty("birthday")
    @NotNull(message = "생년월일은 필수입니다")
    @Min(value = 19000101, message = "올바른 생년월일을 입력해주세요")
    @Max(value = 99991231, message = "올바른 생년월일을 입력해주세요")
    private Integer birthday;

    @JsonProperty("stacks")
    @NotEmpty(message = "최소 1개의 기술 스택을 선택해주세요")
    @Size(max = 10, message = "기술 스택은 최대 10개까지 선택 가능합니다")
    private List<StackInfo> stacks;

    @JsonProperty("positions")
    @NotEmpty(message = "최소 1개의 포지션을 선택해주세요")
    @Size(max = 3, message = "포지션은 최대 3개까지 선택 가능합니다")
    private List<String> positions;

    @JsonProperty("organizations")
    private List<String> organizations;

    @JsonProperty("introduction")
    @Size(max = 500, message = "자기소개는 최대 500자까지 입력 가능합니다")
    private String introduction;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StackInfo {
        @JsonProperty("stackId")
        @NotNull(message = "스택 ID는 필수입니다")
        private Integer stackId;

        @JsonProperty("level")
        @NotNull(message = "스택 레벨은 필수입니다")
        @Min(value = 1, message = "스택 레벨은 1~5 사이여야 합니다")
        @Max(value = 5, message = "스택 레벨은 1~5 사이여야 합니다")
        private Integer level;

        @JsonProperty("isRepresentative")
        @Builder.Default
        private Boolean isRepresentative = false;
    }
}