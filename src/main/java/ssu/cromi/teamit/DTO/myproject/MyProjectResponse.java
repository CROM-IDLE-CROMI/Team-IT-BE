package ssu.cromi.teamit.DTO.myproject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MyProjectResponse {
    private final List<InProgressProject> inProgressProjects;
    private final List<CompletedProject> completedProjects;
}