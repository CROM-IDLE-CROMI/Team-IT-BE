package ssu.cromi.teamit.DTO;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.Notification;
import ssu.cromi.teamit.entity.enums.BoardNotificationType;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private final Long notificationId;
    private final BoardNotificationType notificationType; // 알람 종류
    private final Long targetId;                          // 대상 ID
    private final String message;
    private final boolean isRead;
    private final LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .notificationType(notification.getNotificationType())
                .targetId(notification.getTargetId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}