/*
    프로젝트 생성 요청 DTO
    사용자가 작성한 팀 모집 정보를 서버로 전달할 때 이용
 */
package ssu.cromi.teamit.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
 * 프로젝트 생성 요청 DTO (필드 순서 = Project 엔티티 기준)
 * 클라이언트 요청 → Entity 변환 시 순서 매칭이 쉬움
 */

@Getter
@NoArgsConstructor
public class CreateTeamRequestDto {

    @NotNull(message = "모집 인원 수는 필수 항목입니다.")
    @Min(value = 2, message = "멤버 수는 2명 이상이어야 합니다.")
    private Integer memberNum;

    @NotNull(message = "모집 시작일은 필수 항목입니다.")
    private LocalDateTime validFrom;

    @NotNull(message = "모집 종료일은 필수 항목입니다.")
    private LocalDateTime validTo;

    @NotBlank(message = "플랫폼은 필수 항목입니다.")
    private String platform; // Enum(Platform)

    private String platformDetail;

    @NotEmpty(message = "모집 직군은 최소 하나 이상 입력해야 합니다.")
    private List<@NotBlank(message = "직군 값이 비어있습니다.") String> recruitPositions; // Enum(Position)

    private String recruitDetail;

    @NotEmpty(message = "기술 스택은 최소 하나 이상 입력해야 합니다.")
    private List<@NotBlank(message = "스택 값이 비어있습니다.") String> requireStack;

    @NotBlank(message = "팀 이름은 필수 항목입니다.")
    private String projectName;

    @NotBlank(message = "활동 종류는 필수 항목입니다.")
    private String category; // Enum(Category)

    private String categoryDetail;

    @NotNull(message = "진행 시작일은 필수 항목입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "진행 종료일은 필수 항목입니다.")
    private LocalDateTime endDate;

    @NotNull(message = "프로젝트 시작 예상일은 필수 항목입니다.")
    private LocalDateTime expectedStartDate;

    @NotBlank(message = "작성자의 직군은 필수 항목입니다.")
    private String createrPosition; // 단수형 String

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "프로젝트 진행 상황은 필수 항목입니다.")
    private String projectStatus; // Enum(ProjectStatus)

    private String statusDetail;

    @NotBlank(message = "본문은 필수 항목입니다.")
    private String ideaExplain;

    @NotBlank(message = "회의 방식은 필수 항목입니다.")
    private String meetingApproach; // Enum(MeetingApproach)

    @NotEmpty(message = "위치는 하나 이상 입력해야 합니다.")
    private List<@NotBlank(message = "위치 값이 비어있습니다.") String> locations;

    @NotBlank(message = "지원자 최소 요건은 필수 항목입니다.")
    private String minRequest;

    @NotEmpty(message = "지원자 질문은 하나 이상 입력해야 합니다.")
    private List<@NotBlank(message = "질문이 비어있습니다.") String> applicantQuestions;
}

