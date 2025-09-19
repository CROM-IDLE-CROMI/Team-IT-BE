package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Notification;
import java.util.List;
import ssu.cromi.teamit.entity.enums.BoardNotificationType;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    // 로그인한 사용자의 알림을, 알림 종류에 따라 최신순으로 조회(정렬)
    List<Notification> findByRecipientAndNotificationTypeInOrderByCreatedAtDesc(User recipient, List<BoardNotificationType> types);

    // 특정 알림 삭제
    long deleteByIdAndRecipient(Long notificationId, User recipient);
}