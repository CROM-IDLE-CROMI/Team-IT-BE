package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.BoardComment;

@Getter
public class CommentReplyEvent {
    private final BoardComment reply;

    public CommentReplyEvent(BoardComment reply) {
        this.reply = reply;
    }
}