package ssu.cromi.teamit.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//유저테이블 도메인
@Data // @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "uid", length = 50, nullable = false, updatable = false)
    private String uid;
    @Column(name = "pw", length = 255, nullable = false)
    private String password;
    @Column(name = "nick_name", length = 100, nullable = false)
    private String nickName;
    @Column(name = "email", length = 255, nullable = false)
    private String email;
    @Column(name = "email_verified", length = 1, nullable = false)
    private Boolean emailVerified = false;
    @Column(nullable = false)
    private Integer birthday;
    @Column(nullable = false)
    private Long point;
    @Column(name = "profile_img", length = 2083)
    private String profileImg;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "my_score")
    private Double myScore;
    @Column(name = "roles", length = 50, nullable = false)
    private String roles = "ROLE_USER";


    // 관계 매핑
    @ManyToMany
    @JoinTable(
            name = "user_organizations",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private Set<Organization> organizations = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_stacks",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "stack_id")
    )
    private Set<Stack> Stacks = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_positions",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private Set<Position> positions = new HashSet<>();

    protected User() {}

    public User(String uid, String password, String nickName, String email, Integer birthday) {
        this.uid = uid;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.birthday = birthday;
        this.createdAt = LocalDateTime.now();
    }
    public enum RoleEnum {
        ROLE_USER,
        ROLE_ADMIN
    }
}

