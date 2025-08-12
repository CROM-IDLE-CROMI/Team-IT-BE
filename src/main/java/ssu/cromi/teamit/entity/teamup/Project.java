// DB에 저장될 project 클래스 작성

package ssu.cromi.teamit.entity.teamup;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.Platform;
import ssu.cromi.teamit.entity.enums.Category;
import ssu.cromi.teamit.entity.enums.MeetingApproach;
import ssu.cromi.teamit.entity.enums.ProjectStatus;
import ssu.cromi.teamit.entity.enums.WritingStatus;
import ssu.cromi.teamit.entity.enums.Status;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "project_table")
public class Project {
    // 프로젝트 모집 양식에는 없지만 필요한 내용
    // project_table 내의 필드

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 프로젝트 고유 아이디

    @Column(name = "creator_id", nullable = false, length = 50)
    private String creatorId; // 작성자 아이디

    @Column(name = "owner_id", nullable = false, length = 50)
    private String ownerId; // 팀장 아이디

    // 작성중, 임시저장, 작성완료
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "writing_status", nullable = false)
    private WritingStatus writingStatus = WritingStatus.IN_PROGRESS;

    // 모집중, 모집완료 등
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.RECRUITING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "progress", nullable = false)
    private int progress = 0; // 진행률 (프로젝트 진척도)

    @PrePersist // 생성시각 자동으로 생성
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 수정시각 자동으로 생성
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 실제 프로젝트 모집 양식에 있는 내용
    // project_table 내의 필드

    @Column(name = "member_num", nullable = false)
    private int memberNum; // 모집 인원 (프로젝트 멤버)

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom; // 모집기간 (시작)
    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo; // 모집기간 (종료)

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private Platform platform; // 플랫폼 (웹, 앱, 게임, 기타)

    @Column(name = "platform_detail")
    private String platformDetail; // 플랫폼 기타 작성란

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recruit_positions", columnDefinition = "JSON", nullable = false)
    private List<String> recruitPositions; // 모집 직군 (프론트, 백, 디자인 등)

    @Column(name = "recruit_detail")
    private String recruitDetail; // 모집 직군 기타 작성란

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "require_stack", columnDefinition = "JSON", nullable = false)
    private List<String> requireStack; // 기술 스택 (검색 가능)

    @Column(name = "project_name", nullable = false)
    private String projectName; // 팀 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category; // 활동 종류

    @Column(name = "category_detail")
    private String categoryDetail; // 활동 종류 기타 작성란

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 진행 예상 기간 (시작)
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; // 진행 예상 기간 (종료)

    @Column(name = "expected_start_date", nullable = false)
    private LocalDateTime expectedStartDate; // 프로젝트 예상 시작일

    @Column(name = "title", nullable = false)
    private String title; // 제목

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    private ProjectStatus projectStatus; // 프로젝트 진행 상황

    @Column(name = "status_detail")
    private String statusDetail; // 진행상황 기타 작성란

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String ideaExplain; // 본문

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_approach", nullable = false)
    private MeetingApproach meetingApproach; // 회의 방법

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "locations", columnDefinition = "JSON", nullable = false)
    private List<String> locations; // 위치

    @Column(name = "min_request", columnDefinition = "TEXT", nullable = false)
    private String minRequest; // 지원자 최소 요건

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applicant_questions", columnDefinition = "JSON", nullable = false)
    private List<String> applicantQuestions; // 지원자에게 질문하고 싶은 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers = new ArrayList<>();
}
