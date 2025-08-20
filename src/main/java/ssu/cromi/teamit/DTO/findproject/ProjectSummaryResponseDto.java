package ssu.cromi.teamit.DTO.findproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.teamup.Project;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectSummaryResponseDto {
    private Long projectId;
    private String title;
    private String creatorId; // 작성자 닉네임
    private long viewCount;
    private LocalDateTime createdAt;

    public static ProjectSummaryResponseDto from(Project project) {
        return ProjectSummaryResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .creatorId(project.getCreatorId()) // User 엔티티에서 닉네임 가져오기
                .viewCount(project.getViewCount())
                .createdAt(project.getCreatedAt())
                .build();
    }
}