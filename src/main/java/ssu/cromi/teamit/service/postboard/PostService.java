package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.entity.postboard.Post;

public interface PostService {

    // 게시글 생성 메서드
    Post createPost(PostCreateRequest requestDto, String authorId);
}