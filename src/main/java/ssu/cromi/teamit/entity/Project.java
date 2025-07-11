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
    private Long id;

    private String createrId;
    private String owner;

    private int memberNum;

    @ElementCollection
    private List<String> memberId;

    @ElementCollection
    private List<String> requireStack;
    /*
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String categoryDetail;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    private Boolean premeeting;
    private LocalDateTime premeetingDate;

    @Enumerated(EnumType.STRING)
    private MeetingApproach meetingApproach;

    private String location;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    private String statusDetail;
    private String projectName;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String ideaExplain;

    @Column(columnDefinition = "TEXT")
    private String minRequest;

    @Column(columnDefinition = "TEXT")
    private String questions;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    } */
}
