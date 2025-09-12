package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.Board;
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
    private final String authorProfileImageUrl; // 추가된 필드

    public static PostDetailResponse of(Board board, User author) {
        return PostDetailResponse.builder()
                .postId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory().getDisplayName())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .scrapCount(board.getScrapCount())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .authorId(author.getUid())
                .authorNickname(author.getNickName())
                .authorProfileImageUrl(author.getProfileImg())
                .build();
    }
}
