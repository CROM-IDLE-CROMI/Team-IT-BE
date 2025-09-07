package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.entity.enums.WritingStatus;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.repository.postboard.PostRepository;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post createPost(PostCreateRequest requestDto, String authorId) {
        Post newPost = Post.builder()
                .authorId(authorId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .build();

        newPost.setStatus(WritingStatus.COMPLETED);
        return postRepository.save(newPost);
    }
}