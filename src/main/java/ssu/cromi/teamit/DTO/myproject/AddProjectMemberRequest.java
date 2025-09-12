package ssu.cromi.teamit.DTO.myproject;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddProjectMemberRequest {

    @NotBlank(message = "추가할 멤버의 ID는 필수")
    private String userId;

    @NotBlank(message = "멤버의 직군은 필수")
    private String position;
}
