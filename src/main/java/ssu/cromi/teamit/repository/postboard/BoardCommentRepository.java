package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import java.util.List;

public interface PostCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findAllByPostId(Long postId);
}