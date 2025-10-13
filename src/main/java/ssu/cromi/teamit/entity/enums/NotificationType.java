package ssu.cromi.teamit.entity.enums;

public enum NotificationType {
    // 게시판 관련
    POST_SCRAPPED,           // 내 게시글을 누군가 스크랩
    NEW_COMMENT_ON_POST,     // 내 게시글에 누군가 댓글
    NEW_REPLY_ON_COMMENT,    // 내 댓글에 누군가 대댓글
    COMMENT_LIKED,            // 내 댓글에 누군가 좋아요

    // 프로젝트 관련
    PROJECT_APPLICATION,     // 새로운 지원서 등록
    ADDED_TO_PROJECT,    // 지원서 합격 (프로젝트에 멤버로 추가)
    APPLICATION_DELETED,     // 지원서 탈락 (팀장이 지원서 삭제)

    MEMBER_REMOVED_FROM_PROJECT, // 프로젝트 멤버 삭제
    LEADER_DELEGATED // 팀장으로 임명
}