package ssu.cromi.teamit.entity.enums;

public enum BoardNotificationType {
    POST_SCRAPPED,           // 내 게시글을 누군가 스크랩
    NEW_COMMENT_ON_POST,     // 내 게시글에 누군가 댓글
    NEW_REPLY_ON_COMMENT,    // 내 댓글에 누군가 대댓글
    COMMENT_LIKED            // 내 댓글에 누군가 좋아요
}