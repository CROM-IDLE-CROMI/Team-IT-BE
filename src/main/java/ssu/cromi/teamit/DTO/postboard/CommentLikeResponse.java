package ssu.cromi.teamit.DTO.postboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeResponse {
    private Long commentId;
    private int likeCount;
}