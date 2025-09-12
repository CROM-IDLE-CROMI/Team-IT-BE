package ssu.cromi.teamit.DTO.postboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.entity.enums.PostCategory;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {
    private String title;
    private String content;
    private PostCategory category;
}