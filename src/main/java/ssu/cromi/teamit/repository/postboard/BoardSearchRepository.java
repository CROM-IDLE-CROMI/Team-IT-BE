package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.entity.enums.BoardCategory;
import ssu.cromi.teamit.entity.postboard.Board;

public interface BoardSearchRepository {
    Page<Board> searchBoards(BoardCategory category, String keyword, Pageable pageable);
}