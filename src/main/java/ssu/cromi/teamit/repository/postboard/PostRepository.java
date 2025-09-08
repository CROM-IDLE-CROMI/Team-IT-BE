package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.entity.enums.PostCategory;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    int incrementViewCount(@Param("postId") Long postId);

    /**
     * 특정 카테고리의 모든 게시글을 최신순으로 조회하는 메서드
     * @param category 조회할 카테고리
     * @return 정렬된 게시글 목록
     */
    List<Post> findByCategoryOrderByCreatedAtDesc(PostCategory category);

    /**
     * 모든 게시글을 생성 시각의 최신순으로 조회하는 메서드
     * @return 정렬된 게시글 목록
     */
    List<Post> findAllByOrderByCreatedAtDesc();

    /**
     * 특정 카테고리의 게시글을 페이징하여 조회하는 메서드
     * @param category 조회할 카테고리
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬 순서)
     * @return 페이징된 게시글 목록
     */
    Page<Post> findByCategory(PostCategory category, Pageable pageable);
}