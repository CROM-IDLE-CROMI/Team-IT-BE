package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.BoardCommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentResponse;
import java.util.List;

public interface BoardCommentService {
    void createComment(Long postId, String writerId, BoardCommentRequest dto);
    List<BoardCommentResponse> getCommentsByPostId(Long postId);
    void updateComment(Long commentId, String currentUserId, BoardCommentUpdateRequest dto);
    void deleteComment(Long commentId, String currentUserId);
    void addLike(Long commentId);
}