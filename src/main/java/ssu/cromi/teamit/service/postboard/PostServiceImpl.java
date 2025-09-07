package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.postboard.PostCreateRequest;
import ssu.cromi.teamit.DTO.postboard.PostUpdateRequest;
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

    @Override
    @Transactional
    public Post updatePost(Long postId, PostUpdateRequest requestDto, String currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new SecurityException("게시글을 수정할 권한이 없습니다.");
        } // 애초에 수정하기 버튼이 뜨지 않도록 프론트에서 처리

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setCategory(requestDto.getCategory());

        return post;
    }

    @Override
    @Transactional
    public void deletePost(Long postId, String currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new SecurityException("게시글을 삭제할 권한이 없습니다.");
        } // 삭제하기가 애초에 안뜨도록 프론트에서 처리

        postRepository.delete(post);
    }
}