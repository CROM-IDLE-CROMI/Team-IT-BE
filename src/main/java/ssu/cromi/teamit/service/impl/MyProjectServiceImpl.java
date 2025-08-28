package ssu.cromi.teamit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.myproject.*;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Milestone;
import ssu.cromi.teamit.entity.enums.Position;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.repository.MilestoneRepository;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.service.MyProjectService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyProjectServiceImpl implements MyProjectService{
    private final UserRepository userRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<InProgressProject> getInProgressProjects(String uid){
        User user = findUserByUid(uid);
        List<ProjectMember> memberships = projectMemberRepository.findByUser(user);
        return memberships.stream()
                .map(ProjectMember::getProject)
                .filter(project -> project.getEndDate().isAfter(LocalDateTime.now()))
                .map(project -> InProgressProject.builder()
                        .id(project.getId())
                        .projectName(project.getProjectName())
                        .ownerId(project.getOwner().getNickName())
                        .platform(project.getPlatform())
                        .position(findMyPositionInProject(user, project))
                        .projectStartDate(project.getStartDate())
                        .progressStatus(project.getProjectStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CompletedProject> getCompletedProjects(String uid) {
        User user = findUserByUid(uid);
        List<ProjectMember> memberships = projectMemberRepository.findByUser(user);
        return memberships.stream()
                .map(ProjectMember::getProject)
                .filter(project -> project.getEndDate().isBefore(LocalDateTime.now()))
                .map(project -> CompletedProject.builder()
                        .id(project.getId())
                        .projectName(project.getProjectName())
                        .type(project.getCategory())
                        .position(findMyPositionInProject(user, project))
                        .requireStack(project.getRequireStack())
                        .projectStartDate(project.getStartDate())
                        .isCompleted(true)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public MyProjectResponse getAllMyProjects(String uid) {
        List<InProgressProject> inProgressProjects = getInProgressProjects(uid);
        List<CompletedProject> completedProjects = getCompletedProjects(uid);

        return MyProjectResponse.builder()
                .inProgressProjects(inProgressProjects)
                .completedProjects(completedProjects)
                .build();
    }

    @Override
    @Transactional
    public List<MilestoneResponse> getMilestone(Long projectId){
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다:" + projectId);
        }
        return milestoneRepository.findByProjectId(projectId).stream()
                .map(MilestoneResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MyProjectDetailResponse getMyProjectDetail(Long projectId, int milestoneLimit){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        List<ProjectMemberResponse> members = project.getProjectMembers().stream()
                .map(ProjectMemberResponse::from)
                .toList();

        Pageable pageable = PageRequest.of(0, milestoneLimit, Sort.by(Sort.Direction.ASC, "deadline"));

        List<MilestoneResponse> topMilestones = milestoneRepository
                .findByProjectIdAndProgressLessThan(projectId, 100, pageable)
                .stream()
                .map(MilestoneResponse::from)
                .toList();
        return MyProjectDetailResponse.of(project, members, topMilestones);
    }

    @Override
    @Transactional
    public MilestoneResponse createMilestone(Long projectId, MilestoneRequest milestoneRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        User assignee = userRepository.findByUid(milestoneRequest.getAssigneeName())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + milestoneRequest.getAssigneeName()));
        Milestone milestone = Milestone.builder()
                .project(project)
                .title(milestoneRequest.getTitle())
                .assignee(assignee)
                .deadline(LocalDate.parse(milestoneRequest.getDeadline()))
                .progress(milestoneRequest.getProgress())
                .build();

        Milestone savedMilestone = milestoneRepository.save(milestone);
        return MilestoneResponse.from(savedMilestone);
    }

    @Override
    @Transactional
    public MilestoneResponse updateMilestone(Long projectId, Long milestoneId, MilestoneRequest milestoneRequest){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 마일스톤을 찾을 수 없습니다: " + milestoneId));
        User assignee = userRepository.findByUid(milestoneRequest.getAssigneeName())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + milestoneRequest.getAssigneeName()));
        milestone.setTitle(milestoneRequest.getTitle());
        milestone.setAssignee(assignee);
        milestone.setDeadline(LocalDate.parse(milestoneRequest.getDeadline()));
        milestone.setProgress(milestoneRequest.getProgress());

        Milestone savedMilestone = milestoneRepository.save(milestone);
        return MilestoneResponse.from(savedMilestone);
    }

    @Override
    @Transactional
    public void updateProjectDetails(Long projectId, MyProjectUpdateRequest updateRequestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        project.setProjectName(updateRequestDto.getProjectName());
        project.setProjectLogoUrl(updateRequestDto.getProjectLogoUrl());
        project.setProgress(updateRequestDto.getProgress());
    }

    @Override
    @Transactional
    public void updateProjectDescription(Long projectId, ProjectDescriptionUpdateRequest descriptionRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        project.setIdeaExplain(descriptionRequest.getIdeaExplain());
    }

    private User findUserByUid(String  uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + uid));
    }
    private Position findMyPositionInProject(User user, Project project) {
        return project.getProjectMembers().stream()
                .filter(member -> member.getUser().getUid().equals(user.getUid()))
                .findFirst()
                .map(ProjectMember::getPosition)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectMemberDetailResponse> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));

        return project.getProjectMembers().stream()
                .map(ProjectMemberDetailResponse::from)
                .collect(Collectors.toList());
    }
}