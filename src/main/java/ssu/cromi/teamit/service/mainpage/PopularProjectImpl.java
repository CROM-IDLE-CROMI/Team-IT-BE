/* package ssu.cromi.teamit.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.dto.project.ProjectSummaryResponse;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopularProjectServiceImpl implements PopularProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectSummaryResponse> getPopularProjects() {
        List<Project> projects = projectRepository.findTop4ByOrderByViewCountDesc();
        return projects.stream()
                .map(ProjectSummaryResponse::from)
                .collect(Collectors.toList());
    }
} */