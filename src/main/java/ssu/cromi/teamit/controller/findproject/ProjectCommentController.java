package ssu.cromi.teamit.controller.findproject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentResponseDto;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentRequestDto;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.findproject.ProjectCommentService;
import ssu.cromi.teamit.DTO.findproject.CommentUpdateRequestDto;
import ssu.cromi.teamit.service.impl.UserServiceImpl;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.findproject.ProjectComment;
import ssu.cromi.teamit.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class ProjectCommentController {

    private final ProjectCommentService commentService;
    private final UserServiceImpl userService;

    // 댓글 생성 API
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<ProjectCommentResponseDto>> createComment(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ProjectCommentRequestDto dto
    ) {
        String writerId = userDetails.getUid(); // 로그인한 사용자로 writer 등록
        ProjectComment createdComment = commentService.createComment(projectId, writerId, dto);
        User writer = userService.findByUid(writerId);

        ProjectCommentResponseDto responseDto = ProjectCommentResponseDto.from(createdComment, writer, null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "댓글이 성공적으로 등록되었습니다.", responseDto));
    }

    // 댓글 목록 조회 API
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectCommentResponseDto>> getComments(@PathVariable Long projectId) {
        List<ProjectCommentResponseDto> comments = commentService.getCommentsByProjectId(projectId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정 API
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<ProjectCommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentUpdateRequestDto dto
    ) {
        String currentUserId = userDetails.getUid();
        ProjectComment updatedComment = commentService.updateComment(commentId, currentUserId, dto);
        User writer = userService.findByUid(currentUserId);

        ProjectCommentResponseDto responseDto = ProjectCommentResponseDto.from(updatedComment, writer, null);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "댓글이 성공적으로 수정되었습니다.", responseDto));
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String currentUserId = userDetails.getUid();
        commentService.deleteComment(commentId, currentUserId);
        return ResponseEntity.noContent().build(); // 내용 없이 성공했음을 알림 (204 No Content)
    }

}