package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.CommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.PostCommentRequest;
import ssu.cromi.teamit.DTO.postboard.PostCommentResponse;
import java.util.List;

public interface PostCommentService {
    void createComment(Long postId, String writerId, PostCommentRequest dto);
    List<PostCommentResponse> getCommentsByPostId(Long postId);
    void updateComment(Long commentId, String currentUserId, CommentUpdateRequest dto);
    void deleteComment(Long commentId, String currentUserId);
    void addLike(Long commentId);
}