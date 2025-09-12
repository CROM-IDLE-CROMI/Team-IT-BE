package ssu.cromi.teamit.DTO.findproject;
// 서버가 클라이언트에게 보내주는 데이터 (특정 프로젝트에 달린 문의 댓글 목록)

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
