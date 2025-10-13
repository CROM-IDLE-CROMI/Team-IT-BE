package ssu.cromi.teamit.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ssu.cromi.teamit.event.BoardScrappedEvent;
import ssu.cromi.teamit.event.CommentLikedEvent;
import ssu.cromi.teamit.event.CommentReplyEvent;
import ssu.cromi.teamit.service.NotificationService;
import ssu.cromi.teamit.event.ProjectAppliedEvent;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    // 대댓글 달림 이벤트를 처리
    @EventListener
    public void handleCommentReply(CommentReplyEvent event) {
        notificationService.createNotificationForReply(event.getReply());
    }

    // 스크랩 이벤트 처리
    @EventListener
    public void handleBoardScrapped(BoardScrappedEvent event) {
        notificationService.createNotificationForBoardScrap(event.getScrap());
    }

    // 게시물 댓글에 좋아요 이벤트를 처리
    @EventListener
    public void handleCommentLiked(CommentLikedEvent event) {
        notificationService.createNotificationForCommentLike(event.getComment());
    }

    // 프로젝트 지원을 처리
    @EventListener
    public void handleProjectApplication(ProjectAppliedEvent event) {
        notificationService.createNotificationForApplication(event.getApplication());
    }
}