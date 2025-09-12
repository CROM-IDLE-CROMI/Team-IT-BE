package ssu.cromi.teamit.DTO.findproject;
// 클라이언트가 서버에게 보내는 데이터 (로그인한 사용자가 댓글 등록)

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectCommentRequestDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    // 대댓글일 경우 부모 댓글의 ID, 일반 댓글이면 null
    private Long parentCommentId;
}