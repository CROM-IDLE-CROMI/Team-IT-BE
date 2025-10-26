package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.*;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient; // 알람 받는 사람

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType; // 알람 종류

    @Column(nullable = false)
    private String message; // 알람 메시지

    @Column(name = "target_id")
    private Long targetId; // 알림이 가르키는 게시글 ID

    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false; // 읽음 여부 (읽음 처리 기능은 보류)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}