package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 기능이므로 readOnly = true 설정
public class ProjectSearchServiceImpl implements ProjectSearchService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * Controller로부터 검색 조건(DTO)과 페이징 정보를 받아
     * Repository에 전달하고, 그 결과를 DTO로 변환하여 반환
     */
    @Override
    public Page<ProjectSummaryResponseDto> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable) {

        Page<Project> projectPage = projectRepository.searchProjects(searchDto, pageable);

        List<Project> projects = projectPage.getContent();
        if (projects.isEmpty()) {
            return Page.empty(pageable);
        }

        Map<String, User> userMap = userRepository.findAllById(
                projects.stream()
                        .map(Project::getCreatorId)
                        .collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getUid, u -> u));

        List<ProjectSummaryResponseDto> dtoList = projects.stream()
                .map(project -> {
                    User creator = userMap.get(project.getCreatorId());
                    return ProjectSummaryResponseDto.from(project, creator);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, projectPage.getTotalElements());
    }
}