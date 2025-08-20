/* package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 기능이므로 readOnly = true
public class ProjectSearchService {

    private final ProjectRepository projectRepository;

    public Page<ProjectSummaryResponseDto> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable) {
        return projectRepository.searchProjects(searchDto, pageable)
                .map(ProjectSummaryResponseDto::from); // Page<Project> -> Page<ProjectSummaryResponseDto>
    }
} */