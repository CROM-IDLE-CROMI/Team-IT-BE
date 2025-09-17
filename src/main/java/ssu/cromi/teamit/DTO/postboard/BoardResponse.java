package ssu.cromi.teamit.DTO.postboard;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.Board;
import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private final Long id;
    private final String authorId;
    private final String title;
    private final String content;
    private final String category;
    private final int viewCount;
    private final int likeCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.authorId = board.getAuthorId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.category = board.getCategory().getDisplayName();
        this.viewCount = board.getViewCount();
        this.likeCount = board.getLikeCount();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }
}
