package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentResponseDto;
import ssu.cromi.teamit.entity.findproject.ProjectComment;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.repository.findproject.ProjectCommentRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.DTO.findproject.CommentUpdateRequestDto;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectCommentServiceImpl implements ProjectCommentService {

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
                .filter(c -> c.getParentComment() != null)
                .map(c -> toDto(c, userMap.get(c.getWriterId()), null))
                .collect(Collectors.groupingBy(ProjectCommentResponseDto::getParentCommentId));

        // 4. 부모댓글 DTO 생성 & 대댓글 붙이기
        return allComments.stream()
                .filter(c -> c.getParentComment() == null)
                .map(parent -> {
                    List<ProjectCommentResponseDto> replies = replyMap.getOrDefault(parent.getId(), new ArrayList<>());
                    return toDto(parent, userMap.get(parent.getWriterId()), replies);
                })
                .collect(Collectors.toList());
    }

    /**
     * 댓글 등록 (부모 댓글이 null이면 일반 댓글)
     */
    @Override
    @Transactional
    public void createComment(Long projectId, String writerId, ProjectCommentRequestDto dto) {
        // 1. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        // 2. 부모 댓글 조회 (대댓글일 경우)
        ProjectComment parentComment = null;
        if (dto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        // 3. 댓글 엔티티 생성
        ProjectComment comment = ProjectComment.builder()
                .project(project)
                .writerId(writerId)
                .content(dto.getContent())
                .parentComment(parentComment)
                .build();

        // 4. 저장
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String currentUserId, CommentUpdateRequestDto dto) {
        // 1. 댓글 조회
        ProjectComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        // 2. 권한 확인 (현재 사용자와 댓글 작성자가 동일한지)
        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 수정할 권한이 없습니다."); // 또는 다른 권한 관련 예외 처리
        }

        // 3. 내용 수정
        comment.updateContent(dto.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String currentUserId) {
        // 1. 댓글 조회
        ProjectComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        // 2. 권한 확인
        if (!comment.getWriterId().equals(currentUserId)) {
            throw new SecurityException("댓글을 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        commentRepository.delete(comment);
    }

    /**
     * 댓글 엔티티 -> DTO 변환
     */
    private ProjectCommentResponseDto toDto(ProjectComment entity, User user, List<ProjectCommentResponseDto> replies) {
        return ProjectCommentResponseDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .writerId(entity.getWriterId())
                .writerNickname(user != null ? user.getNickName() : "알 수 없음")
                .writerProfileImageUrl(user != null ? user.getProfileImg() : null)
                .content(entity.getContent())
                .parentCommentId(entity.getParentComment() != null ? entity.getParentComment().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .replies(replies != null ? replies : new ArrayList<>())
                .build();
    }
}
