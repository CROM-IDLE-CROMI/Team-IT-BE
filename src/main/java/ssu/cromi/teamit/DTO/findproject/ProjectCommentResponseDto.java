package ssu.cromi.teamit.DTO.findproject;
// 서버가 클라이언트에게 보내주는 데이터 (특정 프로젝트에 달린 문의 댓글 목록)

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ssu.cromi.teamit.entity.findproject.ProjectComment;
import ssu.cromi.teamit.domain.User;

@Getter
@Builder
public class ProjectCommentResponseDto {
    private Long id;
    private Long projectId;
    private String writerId;
    private String writerNickname;
    private String writerProfileImageUrl;

    private String content;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<ProjectCommentResponseDto> replies = new ArrayList<>(); // 대댓글 목록

    public static ProjectCommentResponseDto from(ProjectComment entity, User user, List<ProjectCommentResponseDto> replies) {
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
