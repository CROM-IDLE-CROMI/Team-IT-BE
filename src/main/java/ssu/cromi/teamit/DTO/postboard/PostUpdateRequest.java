package ssu.cromi.teamit.DTO.postboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.entity.enums.PostCategory;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수 항목입니다.")
    private PostCategory category;
}