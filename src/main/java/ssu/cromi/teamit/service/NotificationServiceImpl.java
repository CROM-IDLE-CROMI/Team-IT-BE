package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.NotificationResponse;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Notification;
import ssu.cromi.teamit.entity.enums.NotificationType;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;
import ssu.cromi.teamit.entity.findproject.ProjectComment;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.entity.teamup.Project;
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
                .notificationType(NotificationType.NEW_REPLY_ON_COMMENT)
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
                .notificationType(NotificationType.POST_SCRAPPED)
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
                .notificationType(NotificationType.COMMENT_LIKED)
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
        List<NotificationType> notificationTypes = List.of(
                NotificationType.POST_SCRAPPED,
                NotificationType.NEW_COMMENT_ON_POST,
                NotificationType.NEW_REPLY_ON_COMMENT,
                NotificationType.COMMENT_LIKED
        );

        return notificationRepository.findByRecipientAndNotificationTypeInOrderByCreatedAtDesc(user, notificationTypes)
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

    // 프로젝트 지원서 알람
    @Override
    @Transactional
    public void createNotificationForApplication(ProjectApplication application) {

        User recipient = application.getProject().getOwner();

        User sender = userRepository.findById(application.getApplicantId())
                .orElseThrow(() -> new UserNotFoundException("지원자 정보를 찾을 수 없습니다."));

        String message = String.format("'%s'님이 '%s' 프로젝트에 지원했습니다.",
                sender.getNickName(), application.getProject().getProjectName());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(NotificationType.PROJECT_APPLICATION)
                .message(message)
                .targetId(application.getId())
                .build();

        notificationRepository.save(notification);
    }

    // 멤버 삭제
    @Override
    @Transactional
    public void createNotificationForMemberRemoval(Project project, User removedUser) {
        // 알람 받을 사람 = 삭제된 멤버
        User recipient = removedUser;

        String message = String.format("'%s' 프로젝트에서 제외되었습니다.", project.getProjectName());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(NotificationType.MEMBER_REMOVED_FROM_PROJECT)
                .message(message)
                .targetId(project.getId()) // 프로젝트 ID를 targetId로 저장
                .build();

        notificationRepository.save(notification);
    }

    // 팀장 위임
    @Override
    @Transactional
    public void createNotificationForNewLeader(Project project, User newLeader) {
        // 알람 받을 사람 = 새로 임명된 팀장
        User recipient = newLeader;

        String message = String.format("'%s' 프로젝트의 새로운 팀장으로 임명되었습니다.", project.getProjectName());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(NotificationType.LEADER_DELEGATED)
                .message(message)
                .targetId(project.getId()) // 프로젝트 ID를 targetId로 저장
                .build();

        notificationRepository.save(notification);
    }

    // 팀원으로 초대 (지원서 합격)
    @Override
    @Transactional
    public void createNotificationForNewMember(Project project, User addedUser) {
        User recipient = addedUser; // 알람 받을 사람 = 새로 추가된 멤버
        String message = String.format("축하합니다! '%s' 프로젝트에 팀원으로 합류하셨습니다.", project.getProjectName());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(NotificationType.ADDED_TO_PROJECT)
                .message(message)
                .targetId(project.getId())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createNotificationForApplicationRejection(ProjectApplication application) {
        // 알람 받을 사람 = 지원자
        User recipient = userRepository.findById(application.getApplicantId())
                .orElseThrow(() -> new UserNotFoundException("지원자 정보를 찾을 수 없습니다."));

        String message = String.format("아쉽게도 '%s' 프로젝트와 함께하지 못하게 되었습니다.",
                application.getProject().getProjectName());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(NotificationType.APPLICATION_DELETED)
                .message(message)
                .targetId(application.getProject().getId())
                .build();
        notificationRepository.save(notification);
    }

    // 내 프로젝트에 댓글 달림
    @Override
    @Transactional
    public void createNotificationForNewProjectComment(ProjectComment comment) {
        // 알람 받을 사람 = 프로젝트 팀장
        User recipient = comment.getProject().getOwner();
        // 댓글 쓴 사람
        User sender = userRepository.findById(comment.getWriterId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 자기 자신의 프로젝트에 댓글을 쓴 경우 알람을 보내지 않음
        if (recipient.equals(sender)) {
            return;
        }

        String message = String.format("'%s'님이 '%s' 프로젝트에 새로운 댓글을 남겼습니다.",
                sender.getNickName(), comment.getProject().getProjectName());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .notificationType(NotificationType.NEW_COMMENT_ON_PROJECT)
                .message(message)
                .targetId(comment.getProject().getId()) // 프로젝트 ID를 targetId로 저장
                .build();

        notificationRepository.save(notification);
    }
}