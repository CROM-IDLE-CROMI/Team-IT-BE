package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class ProjectListServiceImpl implements ProjectListService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ProjectSummaryResponseDto> getProjectList(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByOrderByCreatedAtDesc(pageable);

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