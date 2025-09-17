package ssu.cromi.teamit.DTO.findproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProjectApplicationResponse {
    private final Long applicationId;
    private final Long projectId;
    private final String applicantId;
    private final String title;
    private final String position;
    private final String motivation;
    private final List<String> answers;
    private final Boolean requirements;
    private final LocalDateTime createdAt;

    public static ProjectApplicationResponse of(ProjectApplication application, User applicant) {
        return ProjectApplicationResponse.builder()
                .applicationId(application.getId())
                .projectId(application.getProject().getId())
                .applicantId(applicant.getUid())
                .title(application.getTitle())
                .position(application.getPosition())
                .motivation(application.getMotivation())
                .answers(application.getAnswers())
                .requirements(application.getRequirements())
                .createdAt(application.getCreatedAt())
                .build();
    }
}