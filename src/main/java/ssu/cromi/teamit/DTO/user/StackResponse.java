package ssu.cromi.teamit.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.domain.StackLevel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StackResponse {
    private Integer stackId;
    private StackLevel level;
    private Boolean isRepresentative;
    private String iconUrl;
}