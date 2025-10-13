package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.NotificationResponse;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

import java.util.List;

public interface NotificationService {
    void createNotificationForReply(BoardComment reply);
    void createNotificationForBoardScrap(Scrap scrap);
    void createNotificationForCommentLike(BoardComment comment);

    List<NotificationResponse> getBoardNotifications(String userId);
    void deleteNotification(Long notificationId, String userId);

    void createNotificationForApplication(ProjectApplication application);
    void createNotificationForMemberRemoval(Project project, User removedUser);

    void createNotificationForNewLeader(Project project, User newLeader);
}