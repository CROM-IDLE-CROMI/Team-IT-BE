package ssu.cromi.teamit.DTO.myproject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReviewRequest {
    private Long projectId;
    private String revieweeId;
    private Double score;
    private String content;
}