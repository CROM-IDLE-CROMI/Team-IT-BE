package ssu.cromi.teamit.DTO.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class AwardResponse {
    private String awardName;
    private String organization;
    private LocalDate awardDate;
    private String description;
}