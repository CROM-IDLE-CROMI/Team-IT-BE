package ssu.cromi.teamit.DTO.findproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.enums.Category;
import ssu.cromi.teamit.entity.enums.Platform;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProjectDetailResponseDto {

    private Long projectId;
    private String title;
    private String projectName;
    private LocalDateTime createdAt;
    private long viewCount; // 조회수

    private int memberNum;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    private String platform;
    private String platformDetail;

    private List<String> recruitPositions;
    private List<String> recruitDetail;

    private List<String> requireStack;

    private String category;
    private String categoryDetail;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expectedStartDate;

    private String projectStatus;
    private String statusDetail;

    private String ideaExplain;
    private String meetingApproach;
    private List<String> locations;

    private String minRequest;
    private List<String> applicantQuestions;

    // 작성자 프로필 (팀장 프로필 미리보기) DTO
    // 해당 DTO 변수명은 추후에 마이페이지 작업에 따라 수정하면 됨
    private String creatorId; // 팀장 ID
    private String creatorNickname; // 팀장 닉네임
    private String creatorProfileImageUrl; // 프로필 이미지

    public static ProjectDetailResponseDto from(Project project, User creator) {
        return ProjectDetailResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .projectName(project.getProjectName())
                .creatorId(project.getCreatorId())
                .createdAt(project.getCreatedAt())
                .memberNum(project.getMemberNum())
                .validFrom(project.getValidFrom())
                .validTo(project.getValidTo())
                .platform(project.getPlatform().name())
                .platformDetail(
                        project.getPlatform() == Platform.ETC ? project.getPlatformDetail() : null)
                .recruitPositions(project.getRecruitPositions())
                .requireStack(project.getRequireStack())
                .category(project.getCategory().name())
                .categoryDetail(
                        project.getCategory() == Category.ETC ? project.getCategoryDetail() : null)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .expectedStartDate(project.getExpectedStartDate())
                .projectStatus(project.getStatus().name())
                .statusDetail(
                        project.getStatus() == Status.ETC ? project.getStatusDetail() : null)
                .ideaExplain(project.getIdeaExplain())
                .meetingApproach(project.getMeetingApproach().name())
                .locations(project.getLocations())
                .minRequest(project.getMinRequest())
                .applicantQuestions(project.getApplicantQuestions())
                .viewCount(project.getViewCount())

                .creatorId(project.getCreatorId())
                .creatorNickname(creator.getNickName())
                .creatorProfileImageUrl(creator.getProfileImg())
                .build();
    }
}
