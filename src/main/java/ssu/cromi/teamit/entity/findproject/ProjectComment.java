package ssu.cromi.teamit.entity.findproject;

import jakarta.persistence.*;
import lombok.*;
import ssu.cromi.teamit.entity.teamup.Project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "writer_id", nullable = false, length = 50)
    private String writerId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    // 부모 댓글 (대댓글 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private ProjectComment parentComment;

    // 자식 댓글 목록 (대댓글 관계)
    @Builder.Default
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<ProjectComment> children = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateContent(String content) {
        this.content = content;
    } // 수정을 위해 추가한 코드

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}