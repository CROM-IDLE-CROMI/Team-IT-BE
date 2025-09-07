package ssu.cromi.teamit.DTO.postboard;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.Post;
import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final String authorId;
    private final String title;
    private final String content;
    private final String category;
    private final int viewCount;
    private final int likeCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.authorId = post.getAuthorId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().getDisplayName();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
