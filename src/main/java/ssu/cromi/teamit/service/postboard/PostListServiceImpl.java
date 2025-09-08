package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.postboard.PostListResponse;
import ssu.cromi.teamit.entity.enums.PostCategory;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.PostRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostListServiceImpl implements PostListService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Page<PostListResponse> getPostsByCategory(PostCategory category, Pageable pageable) {
        Page<Post> posts;

        if (category != null) {
            posts = postRepository.findByCategory(category, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }

        List<Post> postContent = posts.getContent();
        List<String> authorIds = postContent.stream().map(Post::getAuthorId).distinct().collect(Collectors.toList());
        Map<String, User> authorsMap = userRepository.findAllById(authorIds).stream().collect(Collectors.toMap(User::getUid, user -> user));

        return posts.map(post -> {
            User author = authorsMap.get(post.getAuthorId());
            return PostListResponse.of(post, author);
        });
    }
}