package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.postboard.CommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.PostCommentRequest;
import ssu.cromi.teamit.DTO.postboard.PostCommentResponse;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.entity.postboard.PostComment;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.PostCommentRepository;
import ssu.cromi.teamit.repository.postboard.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createComment(Long postId, String writerId, PostCommentRequest dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        PostComment parentComment = null;
        if (dto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        PostComment comment = PostComment.builder()
                .post(post)
                .writerId(writerId)
                .content(dto.getContent())
                .parentComment(parentComment)
                .build();

        commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentResponse> getCommentsByPostId(Long postId) {
        List<PostComment> allComments = commentRepository.findAllByPostId(postId);

        Map<String, User> userMap = userRepository.findAllById(
                allComments.stream().map(PostComment::getWriterId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getUid, u -> u));

        Map<Long, List<PostCommentResponse>> replyMap = allComments.stream()
                .filter(c -> c.getParentComment() != null)
                .map(c -> toDto(c, userMap.get(c.getWriterId()), null))
                .collect(Collectors.groupingBy(PostCommentResponse::getParentCommentId));

        return allComments.stream()
                .filter(c -> c.getParentComment() == null)
                .map(parent -> {
                    List<PostCommentResponse> replies = replyMap.getOrDefault(parent.getId(), new ArrayList<>());
                    return toDto(parent, userMap.get(parent.getWriterId()), replies);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String currentUserId, CommentUpdateRequest dto) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 수정할 권한이 없습니다.");
        }
        comment.updateContent(dto.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String currentUserId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void addLike(Long commentId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        comment.setLikeCount(comment.getLikeCount() + 1);
    }

    private PostCommentResponse toDto(PostComment entity, User user, List<PostCommentResponse> replies) {
        return PostCommentResponse.builder()
                .commentId(entity.getId())
                .postId(entity.getPost().getId())
                .writerId(entity.getWriterId())
                .writerNickname(user != null ? user.getNickName() : "알 수 없음")
                .writerProfileImageUrl(user != null ? user.getProfileImg() : null)
                .content(entity.getContent())
                .parentCommentId(entity.getParentComment() != null ? entity.getParentComment().getId() : null)
                .createdAt(entity.getCreatedAt())
                .likeCount(entity.getLikeCount())
                .replies(replies != null ? replies : new ArrayList<>())
                .build();
    }
}