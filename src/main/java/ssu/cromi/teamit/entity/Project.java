package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 프로젝트 고유 아이디

    @Column(nullable = false, length = 50)
    private String createrId; // 작성자 아이디

    @Column(nullable = false, length = 50)
    private String ownerId; // 팀장 아이디

    @Column(nullable = false)
    private int memberNum; // 프로젝트 멤버 수

    @Column(nullable = false)
    private String memberPosition; // 작성자의 역할

    @ElementCollection
    private List<String> memberId;

    @ElementCollection
    private List<String> requireStack;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform; // 프로젝트 종류

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 모집 카테고리

    private String categoryDetail; // 카테고리 기타 작성란

    @Column(nullable = false)
    private LocalDateTime startDate; // 프로젝트 시작일
    @Column(nullable = false)
    private LocalDateTime endDate; // 프로젝트 종료일

    @Column(nullable = false)
    private LocalDateTime validFrom; // 모집 시작기간
    @Column(nullable = false)
    private LocalDateTime validTo; // 모집 종료기간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingApproach meetingApproach; // 회의 방법

    // ** private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus projectStatus; // 진행상황

    private String statusDetail; // 진행상황 기타 작성란

    @Column(nullable = false)
    private String projectName; // 프로젝트 이름

    @Column(nullable = false)
    private String title; // 모집글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String ideaExplain; // 아이디어 설명

    @Column(columnDefinition = "TEXT", nullable = false)
    private String minRequest; // 최소 요건

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questions; // 지원자에게 질문

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
