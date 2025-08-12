package ssu.cromi.teamit.entity.findproject;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import ssu.cromi.teamit.converter.StringListToJsonConverter;
import ssu.cromi.teamit.entity.teamup.Project;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applicant_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지원한 프로젝트 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project; // <팀원 모집>에 해당 Entity Project 존재, merge하면 오류 없어질 것

    // 지원자 ID (users.uid)
    @Column(name = "applicant_id", nullable = false, length = 50)
    private String applicantId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "position", nullable = false, length = 50)
    private String position;

    @Column(name = "motivation", columnDefinition = "TEXT", nullable = false)
    private String motivation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "answer", columnDefinition = "json", nullable = false)
    @Convert(converter = StringListToJsonConverter.class)
    private List<String> answers;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
