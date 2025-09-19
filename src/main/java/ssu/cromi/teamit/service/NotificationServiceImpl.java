package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Notification;
import ssu.cromi.teamit.entity.enums.BoardNotificationType;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.NotificationRepository;
import ssu.cromi.teamit.repository.UserRepository;
import java.lang.String;

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

        if (recipient.equals(sender)) return; //

        String message = String.format("회원님의 '%s' 게시글이 스크랩 되었습니다.", scrap.getBoard().getTitle());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(BoardNotificationType.POST_SCRAPPED)
                .message(message)
                .targetId(scrap.getBoard().getId())
                .build();

        notificationRepository.save(notification);
    }
/*
    @Override
    @Transactional
    public void createNotificationForCommentLike(BoardComment comment, User liker) {
        User recipient = comment.getWriter(); // 댓글 작성자
        if (recipient.equals(liker)) return;

        String message = String.format("'%s'님이 회원님의 댓글을 좋아합니다.", liker.getNickName());
        String url = "/board/" + comment.getBoard().getId();

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(BoardNotificationType.COMMENT_LIKED)
                .message(message)
                .relatedUrl(url)
                .targetId(comment.getBoard().getId())
                .build();
        notificationRepository.save(notification);
    } */
}