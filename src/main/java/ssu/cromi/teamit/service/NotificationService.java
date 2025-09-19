package ssu.cromi.teamit.service;

import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.entity.postboard.Scrap;

public interface NotificationService {
    void createNotificationForReply(BoardComment reply);
    /* void createNotificationForBoardScrap(Scrap scrap);
    void createNotificationForCommentLike(BoardComment comment, User liker);*/
}