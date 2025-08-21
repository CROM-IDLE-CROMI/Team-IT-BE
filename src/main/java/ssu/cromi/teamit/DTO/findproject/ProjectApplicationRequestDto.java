package ssu.cromi.teamit.DTO.findproject;

import jakarta.validation.constraints.*;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectApplicationRequestDto {

    @NotBlank(message = "지원서 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "지원 직군을 선택해주세요.")
    private String position;

    @NotBlank(message = "지원 동기를 입력해주세요.")
    private String motivation;

    @NotNull(message = "최소 요건 충족 여부를 체크해주세요.")
    @AssertTrue(message = "프로젝트의 최소 요건에 동의해야 지원할 수 있습니다.")
    // 해당 필드의 갓이 true가 아닐 경우, 지원서 제출 불가
    private Boolean requirements;
    
    @NotEmpty(message = "질문에 대한 답변을 입력해주세요.")
    private List<@NotBlank(message = "모든 질문에 대해 답변을 입력해주세요.") String> answers;
}
