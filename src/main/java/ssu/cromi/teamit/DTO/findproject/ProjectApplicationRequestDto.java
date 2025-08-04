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

    @NotBlank(message = "질문에 대한 답변을 입력해주세요.")
    private List<@NotBlank(message = "모든 질문에 대해 답변을 입력해주세요.") String> answers;
}
