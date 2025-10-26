package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.entity.teamup.Project;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearchRepository {
    @Modifying
    @Query("UPDATE Board p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    int incrementViewCount(@Param("postId") Long postId);

    // 좋아요 수 높은 순으로 상위 4개를 조회
    List<Board> findTop4ByOrderByScrapCountDesc();

}