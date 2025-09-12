package ssu.cromi.teamit.service.mainpage;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;

import java.util.List;

public interface PopularProject {
    List<ProjectSummaryResponseDto> getPopularProjects();
}
