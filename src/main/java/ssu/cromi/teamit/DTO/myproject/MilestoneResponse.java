package ssu.cromi.teamit.DTO.myproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.Milestone;

@Getter
@Builder
public class MilestoneResponse {
    private Long milestoneId;
    private String title;
    private String assigneeName;
    private String deadline;
    private int progress;

    public static MilestoneResponse from(Milestone milestone){
        return MilestoneResponse.builder()
                .milestoneId(milestone.getId())
                .title(milestone.getTitle())
                .assigneeName(milestone.getAssignee().getNickName())
                .deadline(milestone.getDeadline().toString())
                .progress(milestone.getProgress())
                .build();
    }
}
