package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.NotificationResponse;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Notification;
import ssu.cromi.teamit.entity.enums.BoardNotificationType;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.NotificationRepository;
import ssu.cromi.teamit.repository.UserRepository;
import java.lang.String;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // 대댓글 알람 생성
    @Override
    @Transactional
    public void createNotificationForReply(BoardComment reply) {
        String recipientId = reply.getParentComment().getWriterId();
        String senderId = reply.getWriterId();

        if (recipientId.equals(senderId)) {
            return;
        } // 내 자신에게는 알림을 보내지 않아요

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new UserNotFoundException("알림을 받을 사용자를 찾을 수 없습니다."));

        String boardTitle = reply.getBoard().getTitle();
        Long targetPostId = reply.getBoard().getId();

        String message = String.format("회원님의 '%s' 게시글의 댓글에 답글이 달렸습니다.", boardTitle);

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(BoardNotificationType.NEW_REPLY_ON_COMMENT)
                .message(message)
                .targetId(targetPostId)
                .build();
        notificationRepository.save(notification);
    }

    // 게시글 스크랩
    @Override
    @Transactional
    public void createNotificationForBoardScrap(Scrap scrap) {
        String recipientId = scrap.getBoard().getAuthorId();

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new UserNotFoundException("알림을 받을 사용자를 찾을 수 없습니다.")); // 게시글 작성자

        User sender = scrap.getUser(); // 스크랩한 사람

        if (recipient.equals(sender)) return; // 내 글은 스크랩 알림 안와요

        String message = String.format("회원님의 '%s' 게시글이 스크랩 되었습니다.", scrap.getBoard().getTitle());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(BoardNotificationType.POST_SCRAPPED)
                .message(message)
                .targetId(scrap.getBoard().getId())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createNotificationForCommentLike(BoardComment comment) {
        String recipientId = comment.getWriterId();

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new UserNotFoundException("알림을 받을 사용자를 찾을 수 없습니다."));

        // 이 부분은 프론트엔드에서 자신의 댓글에는 '좋아요' 버튼이 비활성화되도록 해주세요

        String message = String.format("회원님의 댓글이 '%s' 게시글에서 좋아요를 받았습니다.", comment.getBoard().getTitle());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(BoardNotificationType.COMMENT_LIKED)
                .message(message)
                .targetId(comment.getBoard().getId())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getBoardNotifications(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));


        // 좋아요/댓글에 해당하는 알림 종류
        List<BoardNotificationType> boardNotificationTypes = List.of(
                BoardNotificationType.POST_SCRAPPED,
                BoardNotificationType.NEW_COMMENT_ON_POST,
                BoardNotificationType.NEW_REPLY_ON_COMMENT,
                BoardNotificationType.COMMENT_LIKED
        );

        return notificationRepository.findByRecipientAndNotificationTypeInOrderByCreatedAtDesc(user, boardNotificationTypes)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNotification(Long notificationId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        long deletedCount = notificationRepository.deleteByIdAndRecipient(notificationId, user);
    }
}