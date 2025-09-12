package ssu.cromi.teamit.DTO.myproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.entity.teamup.Project;

import java.util.List;

@Getter
@Builder
public class MyProjectDetailResponse {
    private Long projectId;
    private String projectName;
    private String projectStatus;
    private String projectIntro;
    private int overallProgress;
    private String projectLogoUrl;
    private Status status;
    private List<ProjectMemberResponse> members;
    private List<MilestoneResponse> milestones;

    public static MyProjectDetailResponse of(Project project, List<ProjectMemberResponse> members, List<MilestoneResponse> milestones){
        return MyProjectDetailResponse.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .projectLogoUrl(project.getProjectLogoUrl())
                .projectStatus(project.getProjectStatus().getDisplayName())
                .projectIntro(project.getIdeaExplain())
                .overallProgress(project.getProgress())
                .status(project.getStatus())
                .members(members)
                .milestones(milestones)
                .build();
    }
}
