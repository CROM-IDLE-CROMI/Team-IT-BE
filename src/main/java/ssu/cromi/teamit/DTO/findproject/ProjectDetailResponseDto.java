package ssu.cromi.teamit.DTO.findproject;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProjectDetailResponseDto {

    private Long projectId;
    private String title;
    private String projectName;
    private LocalDateTime createdAt;

    private int memberNum;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    private String platform;
    private String platformDetail;

    private List<String> recruitPositions;
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
}
