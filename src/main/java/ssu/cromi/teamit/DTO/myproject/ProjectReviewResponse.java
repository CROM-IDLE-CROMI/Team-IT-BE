package ssu.cromi.teamit.DTO.myproject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReviewResponse {
    private Long id;
    private Long projectId;
    private String reviewerId;
    private String reviewerNickName;
    private String revieweeId;
    private String revieweeNickName;
    private Double score;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}