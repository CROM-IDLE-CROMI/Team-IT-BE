package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findAllByBoardId(Long postId);
}