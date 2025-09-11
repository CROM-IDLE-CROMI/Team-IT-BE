package ssu.cromi.teamit.DTO.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyRatingResponse {
    private Long id;
    private String reviewerId;
    private String reviewerNickName;
    private Double score;
    private String content;
    private LocalDateTime updatedAt;
    private String projectName;
    private String platform;
}
