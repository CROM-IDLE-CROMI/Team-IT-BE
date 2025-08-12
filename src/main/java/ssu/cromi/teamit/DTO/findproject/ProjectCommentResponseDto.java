package ssu.cromi.teamit.DTO.findproject;
// 서버가 클라이언트에게 보내주는 데이터 (특정 프로젝트에 달린 문의 댓글 목록)

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.findproject.ProjectComment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProjectCommentResponseDto {
    private Long id;
    private Long projectId;
    private String writerId;
    private String writerNickname;
    private String writerProfileImageUrl; // 로그인한 사용자에 따른 닉네임이랑 사진 가져오기 추후 서비스에 넣어야함
    // Service에서 writerId에 따른 writerNickname 찾아서 넘기기

    private String content;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<ProjectCommentResponseDto> replies = new ArrayList<>(); // 대댓글 목록
}
