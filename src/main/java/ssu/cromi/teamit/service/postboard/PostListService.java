package ssu.cromi.teamit.service.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.DTO.postboard.PostListResponse;
import ssu.cromi.teamit.entity.enums.PostCategory;

public interface PostListService {
    Page<PostListResponse> getPosts(PostCategory category, String keyword, Pageable pageable);
}