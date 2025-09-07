package ssu.cromi.teamit.controller.postboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.DTO.postboard.PostResponse;
import ssu.cromi.teamit.DTO.postboard.PostUpdateRequest;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.PostService;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String UserId = userDetails.getUid();
        Post createdPost = postService.createPost(requestDto, UserId);

        PostResponse responseDto = new PostResponse(createdPost);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 등록되었습니다.", responseDto)
        );
    }

    @PutMapping("/{postId}") // 게시글 수정
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        Post updatedPost = postService.updatePost(postId, requestDto, userId);
        PostResponse responseDto = new PostResponse(updatedPost);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 수정되었습니다.", responseDto)
        );
    }

    @DeleteMapping("/{postId}") // 게시글 삭제
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        postService.deletePost(postId, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 삭제되었습니다.", null)
        );
    }
}