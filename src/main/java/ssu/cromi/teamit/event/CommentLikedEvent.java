package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.BoardComment;

@Getter
public class CommentLikedEvent {
    private final BoardComment comment;

    public CommentLikedEvent(BoardComment comment) {
        this.comment = comment;
    }
}