package ssu.cromi.teamit.controller.postboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.postboard.BoardCommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentResponse;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.BoardCommentService;
import ssu.cromi.teamit.service.impl.UserServiceImpl;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.DTO.postboard.CommentLikeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/board/{postId}/comments")
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService commentService;
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardCommentResponse>> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardCommentRequest dto) {
        String writerId = userDetails.getUid();
        commentService.createComment(postId, writerId, dto);
        BoardComment createdComment = commentService.createComment(postId, writerId, dto);
        User writer = userService.findByUid(writerId);
        BoardCommentResponse responseDto = BoardCommentResponse.from(createdComment, writer, null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "댓글이 성공적으로 등록되었습니다.", responseDto));
    }

    @GetMapping
    public ResponseEntity<List<BoardCommentResponse>> getComments(@PathVariable Long postId) {
        List<BoardCommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<BoardCommentResponse>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardCommentUpdateRequest dto) {
        String currentUserId = userDetails.getUid();
        BoardComment updatedComment = commentService.updateComment(commentId, currentUserId, dto);
        User writer = userService.findByUid(currentUserId);
        BoardCommentResponse responseDto = BoardCommentResponse.from(updatedComment, writer, null);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "댓글이 성공적으로 수정되었습니다.", responseDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String currentUserId = userDetails.getUid();
        commentService.deleteComment(commentId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentLikeResponse>> addCommentLike(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentLikeResponse responseDto = commentService.addLike(commentId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "좋아요가 반영되었습니다.", responseDto));
    }
}