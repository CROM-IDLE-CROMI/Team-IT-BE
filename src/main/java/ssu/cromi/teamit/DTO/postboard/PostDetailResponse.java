package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.Post;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String category;
    private final int viewCount;
    private final int likeCount;
    private final int scrapCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final String authorId;
    private final String authorNickname;
    private final String writerProfileImageUrl; // 추가된 필드

    public static PostDetailResponse of(Post post, User author) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory().getDisplayName())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .scrapCount(post.getScrapCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorId(author.getUid())
                .authorNickname(author.getNickName())
                .writerProfileImageUrl(author.getProfileImg())
                .build();
    }
}
