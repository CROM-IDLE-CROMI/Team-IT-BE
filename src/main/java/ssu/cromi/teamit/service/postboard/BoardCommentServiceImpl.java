package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.postboard.BoardCommentUpdateRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentRequest;
import ssu.cromi.teamit.DTO.postboard.BoardCommentResponse;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.entity.postboard.BoardComment;
import ssu.cromi.teamit.exception.BoardNotFoundException;
import ssu.cromi.teamit.exception.CommentNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.BoardCommentRepository;
import ssu.cromi.teamit.repository.postboard.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createComment(Long postId, String writerId, BoardCommentRequest dto) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("게시글이 존재하지 않습니다. id=" + postId));

        BoardComment parentComment = null;
        if (dto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글이 존재하지 않습니다. id=" + dto.getParentCommentId()));
        }

        BoardComment comment = BoardComment.builder()
                .board(board)
                .writerId(writerId)
                .content(dto.getContent())
                .parentComment(parentComment)
                .build();

        commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardCommentResponse> getCommentsByPostId(Long postId) {
        List<BoardComment> allComments = commentRepository.findAllByBoardId(postId);

        Map<String, User> userMap = userRepository.findAllById(
                allComments.stream().map(BoardComment::getWriterId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getUid, u -> u));

        Map<Long, List<BoardCommentResponse>> replyMap = allComments.stream()
                .filter(c -> c.getParentComment() != null)
                .map(c -> toDto(c, userMap.get(c.getWriterId()), null))
                .collect(Collectors.groupingBy(BoardCommentResponse::getParentCommentId));

        return allComments.stream()
                .filter(c -> c.getParentComment() == null)
                .map(parent -> {
                    List<BoardCommentResponse> replies = replyMap.getOrDefault(parent.getId(), new ArrayList<>());
                    return toDto(parent, userMap.get(parent.getWriterId()), replies);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String currentUserId, BoardCommentUpdateRequest dto) {
        BoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다. id=" + commentId));

        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 수정할 권한이 없습니다.");
        }
        comment.updateContent(dto.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String currentUserId) {
        BoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다. id=" + commentId));

        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void addLike(Long commentId) {
        BoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다. id=" + commentId));
        comment.setLikeCount(comment.getLikeCount() + 1);
    }

    private BoardCommentResponse toDto(BoardComment entity, User user, List<BoardCommentResponse> replies) {
        return BoardCommentResponse.builder()
                .commentId(entity.getId())
                .postId(entity.getBoard().getId())
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