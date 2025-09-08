package ssu.cromi.teamit.controller.postboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.postboard.CommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.PostCommentRequest;
import ssu.cromi.teamit.DTO.postboard.PostCommentResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.PostCommentService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody PostCommentRequest dto) {
        String writerId = userDetails.getUid();
        commentService.createComment(postId, writerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PostCommentResponse>> getComments(@PathVariable Long postId) {
        List<PostCommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentUpdateRequest dto) {
        String currentUserId = userDetails.getUid();
        commentService.updateComment(commentId, currentUserId, dto);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Void> addCommentLike(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.addLike(commentId);
        return ResponseEntity.ok().build();
    }
}