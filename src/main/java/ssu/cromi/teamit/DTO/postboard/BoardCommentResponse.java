package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PostCommentResponse {
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
    private List<PostCommentResponse> replies = new ArrayList<>();
}