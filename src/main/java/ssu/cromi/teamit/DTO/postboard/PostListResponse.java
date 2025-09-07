package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.Post;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostListResponse {

    private final Long postId;
    private final String title;
    private final String category;
    private final int viewCount;
    private final int likeCount;
    private final LocalDateTime createdAt;
    private final String authorNickname;
    private String writerProfileImageUrl;

    public static PostListResponse of(Post post, User author) {
        return PostListResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .category(post.getCategory().getDisplayName())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .authorNickname(author.getNickName())
                .writerProfileImageUrl(author.getProfileImg())
                .build();
    }
}