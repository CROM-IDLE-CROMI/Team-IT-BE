package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.postboard.PostDetailResponse;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.repository.postboard.PostRepository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostDetailServiceImpl implements PostDetailService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostDetailResponse getPostDetail(Long postId) {
        int updated = postRepository.incrementViewCount(postId);
        if (updated == 0) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + postId);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + postId));

        User author = userRepository.findById(post.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다. id=" + post.getAuthorId()));

        return PostDetailResponse.of(post, author);
    }
}