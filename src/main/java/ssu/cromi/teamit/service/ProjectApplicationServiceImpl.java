package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.dto.ProjectApplicationRequestDto;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.entity.ProjectApplication;
import ssu.cromi.teamit.repository.ProjectApplicationRepository;
import ssu.cromi.teamit.repository.ProjectRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectApplicationServiceImpl implements ProjectApplicationService {

    private final ProjectApplicationRepository projectApplicationRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public void applyToProject(ProjectApplicationRequestDto dto, String applicantId, Long projectId) {
        // 1. 프로젝트 존재 여부 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // 2. 지원서 엔티티 생성
        ProjectApplication application = ProjectApplication.builder()
                .project(project)
                .applicantId(applicantId)
                .title(dto.getTitle())
                .position(dto.getPosition())
                .motivation(dto.getMotivation())
                .answers(dto.getAnswers())
                .createdAt(LocalDateTime.now())
                .build();
        projectApplicationRepository.save(application);
    }
}
