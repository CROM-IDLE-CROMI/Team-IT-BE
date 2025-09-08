package ssu.cromi.teamit.controller.postboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.DTO.common.PageResponse;
import ssu.cromi.teamit.DTO.postboard.PostUpdateRequest;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.PostService;
import ssu.cromi.teamit.DTO.postboard.PostListResponse;
import ssu.cromi.teamit.service.postboard.PostListService;
import ssu.cromi.teamit.service.postboard.PostDetailService;
import ssu.cromi.teamit.DTO.postboard.PostDetailResponse;
import ssu.cromi.teamit.entity.enums.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import ssu.cromi.teamit.DTO.postboard.PostResponse;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostDetailService postDetailService;
    private final PostListService postListService;

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

    @PatchMapping("/{postId}") // 게시글 수정
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

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostById(@PathVariable Long postId) {
        PostDetailResponse responseDto = postDetailService.getPostDetail(postId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 조회되었습니다.", responseDto)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostListResponse>>> getPosts(
            @RequestParam(value = "category", defaultValue = "INFO") PostCategory category,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostListResponse> postPage = postListService.getPostsByCategory(category, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글 목록이 성공적으로 조회되었습니다.", PageResponse.of(postPage))
        );
    }
}