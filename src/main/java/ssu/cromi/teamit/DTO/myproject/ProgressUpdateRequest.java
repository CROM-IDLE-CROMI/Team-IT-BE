package ssu.cromi.teamit.DTO.myproject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProgressUpdateRequest {
    @NotNull(message = "진척도 값은 필수입니다")
    @Min(value = 0,message = "진척도는 0 이상이어야 합니다.")
    @Max(value = 100, message = "진척도는 100 이하여야 합니다")
    private int progress;
}