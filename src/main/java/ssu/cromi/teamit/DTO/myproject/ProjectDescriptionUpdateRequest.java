package ssu.cromi.teamit.DTO.myproject;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDescriptionUpdateRequest {
    @NotBlank(message = "프로젝트 소개는 필수입니다.")
    private String ideaExplain;
}