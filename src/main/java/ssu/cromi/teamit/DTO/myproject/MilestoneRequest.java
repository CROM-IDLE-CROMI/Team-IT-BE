package ssu.cromi.teamit.DTO.myproject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MilestoneRequest {
    private String title;
    private String assigneeName;
    private String deadline;
    private int progress;
}
