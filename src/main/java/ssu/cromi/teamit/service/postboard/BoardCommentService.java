package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.BoardCommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentResponse;
import ssu.cromi.teamit.DTO.postboard.CommentLikeResponse;
import ssu.cromi.teamit.entity.postboard.BoardComment;

import java.util.List;

public interface BoardCommentService {
    BoardComment createComment(Long postId, String writerId, BoardCommentRequest dto);
    List<BoardCommentResponse> getCommentsByPostId(Long postId);
    BoardComment updateComment(Long commentId, String currentUserId, BoardCommentUpdateRequest dto);
    void deleteComment(Long commentId, String currentUserId);
    CommentLikeResponse addLike(Long commentId, String likerId);
}