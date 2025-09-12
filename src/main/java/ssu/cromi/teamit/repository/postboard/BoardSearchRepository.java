package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.entity.enums.PostCategory;
import ssu.cromi.teamit.entity.postboard.Board;

public interface PostSearchRepository {
    Page<Board> searchPosts(PostCategory category, String keyword, Pageable pageable);
}