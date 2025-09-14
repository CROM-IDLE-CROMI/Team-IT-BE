package ssu.cromi.teamit.DTO.postboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.entity.enums.BoardCategory;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {
    private String title;
    private String content;
    private BoardCategory category;
}