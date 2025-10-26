package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.Board;
import java.time.LocalDateTime;

@Getter
@Builder
public class BoardListResponse {

    private final Long postId;
    private final String title;
    private final String category;
    private final int viewCount;
    private final int likeCount;
    private final LocalDateTime createdAt;
    private final String authorNickname;
    private String authorProfileImageUrl;

    public static BoardListResponse of(Board board, User author) {
        return BoardListResponse.builder()
                .postId(board.getId())
                .title(board.getTitle())
                .category(board.getCategory().getDisplayName())
                .viewCount(board.getViewCount())
                .createdAt(board.getCreatedAt())
                .authorNickname(author.getNickName())
                .authorProfileImageUrl(author.getProfileImg())
                .build();
    }
}