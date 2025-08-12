package ssu.cromi.teamit.DTO.findproject;

import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProjectApplyPageResponseDto {

    // 본문 내용 (UI 우측 영역)
    private ProjectDetailResponseDto projectDetail;

    // 지원서 입력값 (UI 좌측 영역)
    private String title;
    private String position;
    private String motivation;
    private List<String> answers;
}
