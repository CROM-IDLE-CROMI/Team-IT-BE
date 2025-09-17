package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.domain.User;

@Getter
@Builder
public class BoardCommentResponse {
    private Long commentId;
    private Long postId;
    private String writerId;
    private String writerNickname;
    private String writerProfileImageUrl;
    private String content;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private int likeCount;

    @Builder.Default
    private List<BoardCommentResponse> replies = new ArrayList<>();

    public static BoardCommentResponse from(BoardComment entity, User user, List<BoardCommentResponse> replies) {
        return BoardCommentResponse.builder()
                .commentId(entity.getId())
                .postId(entity.getBoard().getId())
                .writerId(entity.getWriterId())
                .writerNickname(user != null ? user.getNickName() : "알 수 없음")
                .writerProfileImageUrl(user != null ? user.getProfileImg() : null)
                .content(entity.getContent())
                .parentCommentId(entity.getParentComment() != null ? entity.getParentComment().getId() : null)
                .createdAt(entity.getCreatedAt())
                .likeCount(entity.getLikeCount())
                .replies(replies != null ? replies : new ArrayList<>())
                .build();
    }
}