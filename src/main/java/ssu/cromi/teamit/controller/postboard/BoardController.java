package ssu.cromi.teamit.controller.postboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.common.PageResponse;
import ssu.cromi.teamit.DTO.postboard.BoardCreateRequest;
import ssu.cromi.teamit.DTO.postboard.BoardDetailResponse;
import ssu.cromi.teamit.DTO.postboard.BoardListResponse;
import ssu.cromi.teamit.DTO.postboard.BoardUpdateRequest;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.BoardCategory;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.impl.UserServiceImpl;
import ssu.cromi.teamit.service.postboard.BoardDetailService;
import ssu.cromi.teamit.service.postboard.BoardListService;
import ssu.cromi.teamit.service.postboard.BoardService;


@RestController
@RequestMapping("/v1/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardDetailService boardDetailService;
    private final BoardListService boardListService;
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardDetailResponse>> createPost(
            @Valid @RequestBody BoardCreateRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String UserId = userDetails.getUid();
        Board createdBoard = boardService.createBoard(requestDto, UserId);

        User author = userService.findByUid(UserId);
        BoardDetailResponse responseDto = BoardDetailResponse.of(createdBoard, author);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(201, "게시글이 성공적으로 등록되었습니다.", responseDto)
        );
    }

    @PatchMapping("/{postId}") // 게시글 수정
    public ResponseEntity<ApiResponse<BoardDetailResponse>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody BoardUpdateRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        Board updatedBoard = boardService.updateBoard(postId, requestDto, userId);

        User author = userService.findByUid(userId);
        BoardDetailResponse responseDto = BoardDetailResponse.of(updatedBoard, author);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 수정되었습니다.", responseDto)
        );
    }

    @DeleteMapping("/{postId}") // 게시글 삭제
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        boardService.deleteBoard(postId, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 삭제되었습니다.", null)
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getPostById(@PathVariable Long postId) {
        BoardDetailResponse responseDto = boardDetailService.getBoardDetail(postId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글이 성공적으로 조회되었습니다.", responseDto)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BoardListResponse>>> getPosts(
            @RequestParam(value = "category", defaultValue = "INFO") BoardCategory category,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BoardListResponse> postPage = boardListService.getBoards(category, keyword, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시글 목록이 성공적으로 조회되었습니다.", PageResponse.of(postPage))
        );
    }
}