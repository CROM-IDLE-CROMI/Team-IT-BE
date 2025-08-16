package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.*;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "milestones")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", referencedColumnName = "uid", nullable = false)
    private User assignee; //담당자

    private LocalDate deadline;

    private int progress; //진척도

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
