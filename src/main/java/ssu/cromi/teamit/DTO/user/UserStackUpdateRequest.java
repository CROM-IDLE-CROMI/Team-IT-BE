package ssu.cromi.teamit.DTO.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ssu.cromi.teamit.domain.StackLevel;

import java.util.List;

public class UserStackUpdateRequest {
    
    @NotNull(message = "스택 정보는 필수입니다")
    @Getter
    @Setter
    private List<StackInfo> stacks;
    
    @Getter
    @Setter
    public static class StackInfo {
        @NotNull(message = "스택 ID는 필수입니다")
        private Integer stackId;
        
        @NotNull(message = "스택 레벨은 필수입니다")
        private StackLevel level;
        
        private Boolean isRepresentative = false;
    }
}