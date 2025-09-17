package ssu.cromi.teamit.service.findproject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;

public interface ProjectSearchService {
    Page<ProjectSummaryResponseDto> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable);
}