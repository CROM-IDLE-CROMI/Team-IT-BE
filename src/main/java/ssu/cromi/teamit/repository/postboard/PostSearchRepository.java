package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.entity.enums.PostCategory;
import ssu.cromi.teamit.entity.postboard.Post;

public interface PostSearchRepository {
    Page<Post> searchPosts(PostCategory category, String keyword, Pageable pageable);
}