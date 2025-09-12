package ssu.cromi.teamit.DTO.myproject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyProjectUpdateRequest {
    @NotBlank(message = "팀 이름은 필수입니다.")
    private String projectName;

    private String projectLogoUrl;

    @NotNull(message = "진척도 값은 필수입니다")
    @Min(value = 0,message = "진척도는 0 이상이어야 합니다.")
    @Max(value = 100, message = "진척도는 100 이하여야 합니다")
    private int progress;
}