package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.DTO.postboard.PostUpdateRequest;

public interface PostService {

    // 게시글 생성 메서드
    Board createPost(PostCreateRequest requestDto, String authorId);

    // 게시글 수정
    Board updatePost(Long postId, PostUpdateRequest requestDto, String currentUserId);

    // 게시글 삭제
    void deletePost(Long postId, String currentUserId);
}
