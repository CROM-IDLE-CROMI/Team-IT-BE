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

import java.util.List;

@RestController
@RequestMapping("/v1/projects/{projectId}/comments")
@RequiredArgsConstructor
public class ProjectCommentController {

    private final ProjectCommentService commentService;

    // 댓글 생성 API
    @PostMapping
    public ResponseEntity<Void> createComment(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ProjectCommentRequestDto dto
    ) {
        String writerId = userDetails.getUid(); // 로그인한 사용자로 writer 등록
        commentService.createComment(projectId, writerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 댓글 목록 조회 API
    @GetMapping
    public ResponseEntity<List<ProjectCommentResponseDto>> getComments(@PathVariable Long projectId) {
        List<ProjectCommentResponseDto> comments = commentService.getCommentsByProjectId(projectId);
        return ResponseEntity.ok(comments);
    }
}