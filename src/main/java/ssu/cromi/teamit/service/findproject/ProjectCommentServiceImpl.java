package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentResponseDto;
import ssu.cromi.teamit.entity.findproject.ProjectComment;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.exception.CommentNotFoundException;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.repository.findproject.ProjectCommentRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.DTO.findproject.CommentUpdateRequestDto;
import ssu.cromi.teamit.event.ProjectCommentCreatedEvent;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectCommentServiceImpl implements ProjectCommentService {

    private final ApplicationEventPublisher eventPublisher;
    private final ProjectCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectCommentResponseDto> getCommentsByProjectId(Long projectId) {
        // 1. 전체 댓글 조회
        List<ProjectComment> allComments = commentRepository.findAllByProjectId(projectId);

        // 2. 작성자 정보 한번에 조회, set으로 중복 작성자 없애기
        Map<String, User> userMap = userRepository.findAllById(
                allComments.stream()
                        .map(ProjectComment::getWriterId)
                        .collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getUid, u -> u));

        // 3. 대댓글만 필터링 후 부모ID로 그룹핑
        Map<Long, List<ProjectCommentResponseDto>> replyMap = allComments.stream()
                .filter(comment -> comment.getParentComment() != null)
                .map(comment -> ProjectCommentResponseDto.from(comment, userMap.get(comment.getWriterId()), null))
                .collect(Collectors.groupingBy(ProjectCommentResponseDto::getParentCommentId));

        // 4. 부모댓글 DTO 생성 & 대댓글 붙이기
        return allComments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(parentComment -> {
                    // 부모 댓글에 해당하는 대댓글 목록을 replyMap에서 찾아옵니다.
                    List<ProjectCommentResponseDto> replies = replyMap.getOrDefault(parentComment.getId(), new ArrayList<>());
                    // 부모 댓글과 대댓글 목록을 합쳐 최종 DTO를 생성합니다.
                    return ProjectCommentResponseDto.from(parentComment, userMap.get(parentComment.getWriterId()), replies);
                })
                .collect(Collectors.toList());
    }

    /**
     * 댓글 등록 (부모 댓글이 null이면 일반 댓글)
     */
    @Override
    @Transactional
    public ProjectComment createComment(Long projectId, String writerId, ProjectCommentRequestDto dto) {
        // 1. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트가 존재하지 않습니다. id=" + projectId));

        // 2. 부모 댓글 조회 (대댓글일 경우)
        ProjectComment parentComment = null;
        if (dto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글이 존재하지 않습니다. id=" + dto.getParentCommentId()));
        }

        // 3. 댓글 엔티티 생성
        ProjectComment comment = ProjectComment.builder()
                .project(project)
                .writerId(writerId)
                .content(dto.getContent())
                .parentComment(parentComment)
                .build();

        ProjectComment savedComment = commentRepository.save(comment);

        if (savedComment.getParentComment() == null) {
            eventPublisher.publishEvent(new ProjectCommentCreatedEvent(savedComment));
        }

        return savedComment;
    }

    @Override
    @Transactional
    public ProjectComment updateComment(Long commentId, String currentUserId, CommentUpdateRequestDto dto) {
        // 1. 댓글 조회
        ProjectComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다. id=" + commentId));

        // 2. 권한 확인 (현재 사용자와 댓글 작성자가 동일한지)
        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 수정할 권한이 없습니다."); // 또는 다른 권한 관련 예외 처리
        }

        // 3. 내용 수정
        comment.updateContent(dto.getContent());
        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String currentUserId) {
        // 1. 댓글 조회
        ProjectComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다. id= " + commentId));

        // 2. 권한 확인
        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        commentRepository.delete(comment);
    }

}
