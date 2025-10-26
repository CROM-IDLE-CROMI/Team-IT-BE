package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.domain.User;

@Getter
public class CommentLikedEvent {
    private final BoardComment comment;

    public CommentLikedEvent(BoardComment comment) {
        this.comment = comment;
    }
}