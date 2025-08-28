package ssu.cromi.teamit.entity.teamup;

import jakarta.persistence.*;
import lombok.*;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.MemberRole;
import ssu.cromi.teamit.entity.enums.Position;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Project_Member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK (프로젝트 멤버 테이블 고유 PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false) // FK (project_table.id)
    private Project project;


    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private String userId; // 작성자 포함 팀원 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role; // 팀장 or 팀원

    @Enumerated(EnumType.STRING)
    @Column(name = "member_position", nullable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "project_member_stacks",
            joinColumns = @JoinColumn(name = "project_member_id"),
            inverseJoinColumns = @JoinColumn(name = "stack_id")
    )
    @Builder.Default
    private Set<Stack> projectStacks = new HashSet<>();
}