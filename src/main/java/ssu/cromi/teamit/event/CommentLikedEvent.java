package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.domain.User;

@Getter
public class CommentLikedEvent {
    private final BoardComment comment;
    private final User liker; // 좋아요 누른 사람

    public CommentLikedEvent(BoardComment comment, User liker) {
        this.comment = comment;
        this.liker = liker;
    }
}