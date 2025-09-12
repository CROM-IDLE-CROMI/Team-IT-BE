package ssu.cromi.teamit.DTO.myproject;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProjectMemberRequest {

    @NotBlank(message = "멤버의 직군은 필수")
    private String position;
    
    private String role;
    
    private List<String> techStacks;
}