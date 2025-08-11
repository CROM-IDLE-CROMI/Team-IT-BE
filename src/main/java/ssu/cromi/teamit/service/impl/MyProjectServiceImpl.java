package ssu.cromi.teamit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.dto.myproject.CompletedProject;
import ssu.cromi.teamit.dto.myproject.InProgressProject;
import ssu.cromi.teamit.dto.myproject.MyProjectResponse;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.entity.ProjectMember;
import ssu.cromi.teamit.entity.enums.Position;
import ssu.cromi.teamit.repository.ProjectMemberRepository;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.service.MyProjectService;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyProjectServiceImpl implements MyProjectService{
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

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

    private User findUserByUid(String  uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + uid));
    }
    private Position findMyPositionInProject(User user, Project project) {
        return project.getProjectMembers().stream()
                // .getId() 대신 .getUid()를 사용하도록 수정합니다.
                .filter(member -> member.getUser().getUid().equals(user.getUid()))
                .findFirst()
                .map(ProjectMember::getPosition)
                .orElse(null);
    }
}