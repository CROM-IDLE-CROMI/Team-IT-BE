package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.NotificationResponse;
import ssu.cromi.teamit.entity.Notification;
import ssu.cromi.teamit.entity.enums.NotificationType;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public Page<NotificationResponse> getNotifications(String userId, String type, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        List<NotificationType> typesToFetch = List.of(
                NotificationType.PROJECT_APPLICATION,
                NotificationType.APPLICATION_DELETED,
                NotificationType.ADDED_TO_PROJECT,
                NotificationType.MEMBER_REMOVED_FROM_PROJECT,
                NotificationType.LEADER_DELEGATED,
                NotificationType.NEW_COMMENT_ON_PROJECT
        );

        if ("BOARD".equalsIgnoreCase(type)) {
            // '좋아요/댓글' 탭: 게시판 관련 알림만
            typesToFetch = List.of(
                    NotificationType.POST_SCRAPPED,
                    NotificationType.NEW_COMMENT_ON_POST,
                    NotificationType.NEW_REPLY_ON_COMMENT,
                    NotificationType.COMMENT_LIKED
            );
        } else if ("PROJECT".equalsIgnoreCase(type)) {
            // '프로젝트' 탭: 프로젝트 관련 알림만
            typesToFetch = List.of(
                    NotificationType.PROJECT_APPLICATION,
                    NotificationType.APPLICATION_DELETED,
                    NotificationType.ADDED_TO_PROJECT,
                    NotificationType.MEMBER_REMOVED_FROM_PROJECT,
                    NotificationType.LEADER_DELEGATED,
                    NotificationType.NEW_COMMENT_ON_POST
            );
        }

        Page<Notification> notificationPage = notificationRepository.findByRecipientAndNotificationTypeIn(user, typesToFetch, pageable);

        return notificationPage.map(NotificationResponse::from);
    }
}