package ssu.cromi.teamit.entity.postboard;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.domain.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "scraps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ScrapId.class) // 복합키 클래스 지정
public class Scrap {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "uid") // users 테이블의 uid 컬럼과 연결
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Board board;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Scrap(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}