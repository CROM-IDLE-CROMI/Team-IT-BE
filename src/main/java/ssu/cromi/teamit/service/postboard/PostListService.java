package ssu.cromi.teamit.service.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.DTO.postboard.PostListResponse;
import ssu.cromi.teamit.entity.enums.PostCategory;

public interface PostListService {

    /**
     * 카테고리별로 게시글 목록을 페이징하여 조회하는 메서드
     * @param category 조회할 카테고리
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 게시글 목록 DTO
     */
    Page<PostListResponse> getPostsByCategory(PostCategory category, Pageable pageable);
}