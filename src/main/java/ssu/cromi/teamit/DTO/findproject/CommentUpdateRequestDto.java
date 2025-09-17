package ssu.cromi.teamit.DTO.findproject;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Postman 테스트 시 Body 파싱을 위해 필요
public class CommentUpdateRequestDto {

    @NotBlank(message = "수정할 댓글 내용을 입력해주세요.")
    private String content;
}