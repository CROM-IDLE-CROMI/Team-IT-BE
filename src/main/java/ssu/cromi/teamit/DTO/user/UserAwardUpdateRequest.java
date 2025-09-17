package ssu.cromi.teamit.DTO.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class UserAwardUpdateRequest {
    
    @NotNull(message = "수상 정보는 필수입니다")
    @Getter
    @Setter
    private List<AwardInfo> awards;
    
    @Getter
    @Setter
    public static class AwardInfo {
        private String awardName;
        private String organization;
        private LocalDate awardDate;
        private String description;
    }
}