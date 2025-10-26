package ssu.cromi.teamit.DTO.findproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProjectSummaryResponseDto {
    private Long projectId;
    private String title;
    private String projectName;
    private String projectStatus;

    private List<String> recruitPositions;
    private List<String> requireStack;
    private String creatorId; // 작성자 닉네임
    private long viewCount;
    private LocalDateTime createdAt;

    private String creatorNickname;
    private String creatorProfileImageUrl;

    public static ProjectSummaryResponseDto from(Project project, User creator) {
        return ProjectSummaryResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .projectName(project.getProjectName())
                .projectStatus(project.getProjectStatus().name())
                .recruitPositions(project.getRecruitPositions())
                .requireStack(project.getRequireStack())
                .viewCount(project.getViewCount())
                .createdAt(project.getCreatedAt())
                .creatorNickname(creator.getNickName())
                .creatorProfileImageUrl(creator.getProfileImg())
                .build();
    }
}