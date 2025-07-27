package ssu.cromi.teamit.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectApplicationRequestDto {

    @NotBlank(message = "지원서 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "지원 직군을 선택해주세요.")
    private String position;

    @NotBlank(message = "지원 동기를 입력해주세요.")
    private String motivation;

    @NotBlank(message = "질문에 대한 답변을 입력해주세요.")
    private List<@NotBlank(message = "답변이 비어있을 수 없습니다.") String> answers;
}
