package ssu.cromi.teamit.controller.postboard;

import jakarta.validation.Valid; // @Valid import
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // @AuthenticationPrincipal import
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.DTO.postboard.PostResponse;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.response.ApiResponse; // ApiResponse import
import ssu.cromi.teamit.security.UserDetailsImpl; // UserDetailsImpl import
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
}