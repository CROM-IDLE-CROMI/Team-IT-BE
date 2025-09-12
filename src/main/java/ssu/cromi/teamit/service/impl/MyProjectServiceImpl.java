package ssu.cromi.teamit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.myproject.*;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Milestone;
import ssu.cromi.teamit.entity.enums.MemberRole;
import ssu.cromi.teamit.entity.enums.Position;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.exceptions.LeaderDeletionException;
import ssu.cromi.teamit.repository.MilestoneRepository;
import ssu.cromi.teamit.repository.StackRepository;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.service.MyProjectService;
import ssu.cromi.teamit.util.EnumValidator;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyProjectServiceImpl implements MyProjectService{
    private final UserRepository userRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final StackRepository stackRepository;

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
                        .status(project.getStatus())
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
                        .status(project.getStatus())
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

    @Override
    @Transactional
    public ProjectMemberResponse addProjectMember(Long projectId, AddProjectMemberRequest requestDto){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다" + projectId));
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다" + requestDto.getUserId()));
        Position position = EnumValidator.parseEnum(Position.class, requestDto.getPosition(),"position");

        boolean isAlreadyMember = project.getProjectMembers().stream()
                .anyMatch(member -> member.getUser().getUid().equals(requestDto.getUserId()));
        if(isAlreadyMember){
            throw new IllegalArgumentException("해당 프로젝트에 이미 참여중인 멤버입니다.");
        }

        ProjectMember newMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(MemberRole.MEMBER) // 기본 팀원
                .position(position)
                .build();

        ProjectMember savedMember = projectMemberRepository.save(newMember);

        return ProjectMemberResponse.from(savedMember);
    }

    @Override
    @Transactional
    public ProjectMemberResponse updateProjectMember(Long projectId, String userId, UpdateProjectMemberRequest requestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 프로젝트에서 멤버를 찾을 수 없습니다: " + userId));

        Position newPosition = EnumValidator.parseEnum(Position.class, requestDto.getPosition(), "position");
        
        // 기술스택 처리 (없으면 자동 생성)
        Set<Stack> projectStacks = new HashSet<>();
        if (requestDto.getTechStacks() != null && !requestDto.getTechStacks().isEmpty()) {
            for (String stackTag : requestDto.getTechStacks()) {
                Stack stack = stackRepository.findByTag(stackTag)
                        .orElseGet(() -> createNewStack(stackTag));
                projectStacks.add(stack);
            }
        }
        
        ProjectMember updatedMember = ProjectMember.builder()
                .id(member.getId())
                .project(member.getProject())
                .userId(member.getUserId())
                .user(member.getUser())
                .role(requestDto.getRole() != null ? 
                    EnumValidator.parseEnum(MemberRole.class, requestDto.getRole(), "role") : 
                    member.getRole())
                .position(newPosition)
                .projectStacks(projectStacks)
                .build();

        ProjectMember savedMember = projectMemberRepository.save(updatedMember);

        return ProjectMemberResponse.from(savedMember);
    }

    private Stack createNewStack(String stackTag) {
        try {
            // 리플렉션을 사용해서 Stack 엔티티 생성
            Constructor<Stack> constructor = Stack.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Stack newStack = constructor.newInstance();
            newStack.setTag(stackTag);
            newStack.setIcon(stackTag.toLowerCase() + "-icon.png");
            
            Stack savedStack = stackRepository.save(newStack);
            log.info("새로운 기술스택 자동 생성: {}", stackTag);
            return savedStack;
        } catch (Exception e) {
            throw new RuntimeException("기술스택 생성 실패: " + stackTag, e);
        }
    }

    @Override
    @Transactional
    public void deleteProjectMember(Long projectId, String userId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 프로젝트에서 멤버를 찾을 수 없습니다: " + userId));
        if(member.getRole() == MemberRole.LEADER){
            throw new LeaderDeletionException("팀장은 프로젝트에서 삭제할 수 없습니다");
        }
        User user = member.getUser();
        List<Milestone> assignedMilestones = milestoneRepository.findByProjectIdAndAssignee(projectId,user);
        
        // 프로젝트 리더를 찾아서 마일스톤 재할당
        User projectLeader = project.getProjectMembers().stream()
                .filter(m -> m.getRole() == MemberRole.LEADER)
                .findFirst()
                .map(ProjectMember::getUser)
                .orElse(project.getOwner()); // 리더가 없으면 프로젝트 소유자에게 할당
        
        for(Milestone milestone : assignedMilestones){
            milestone.setAssignee(projectLeader);
        }

        projectMemberRepository.delete(member);
        log.info("프로젝트(ID : {})에서 멤버(ID: {})를 삭제했습니다.", projectId, userId);
    }

    @Override
    @Transactional
    public void delegateLeadership(Long projectId, DelegateLeadershipRequest requestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));

        String newLeaderUserId = requestDto.getNewLeaderUserId();
        
        // 새로운 팀장이 될 멤버가 프로젝트에 참여 중인지 확인
        ProjectMember newLeaderMember = projectMemberRepository.findByProjectIdAndUserId(projectId, newLeaderUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 프로젝트 멤버가 아닙니다: " + newLeaderUserId));

        // 현재 팀장 찾기
        ProjectMember currentLeader = projectMemberRepository.findByProjectAndRole(project, MemberRole.LEADER)
                .orElse(null);

        // 새로운 팀장이 이미 팀장인지 확인
        if (newLeaderMember.getRole() == MemberRole.LEADER) {
            throw new IllegalArgumentException("해당 멤버는 이미 팀장입니다.");
        }

        // 현재 팀장을 멤버로 변경
        if (currentLeader != null) {
            ProjectMember updatedCurrentLeader = ProjectMember.builder()
                    .id(currentLeader.getId())
                    .project(currentLeader.getProject())
                    .userId(currentLeader.getUserId())
                    .user(currentLeader.getUser())
                    .role(MemberRole.MEMBER)
                    .position(currentLeader.getPosition())
                    .projectStacks(currentLeader.getProjectStacks())
                    .build();
            projectMemberRepository.save(updatedCurrentLeader);
        }

        // 새로운 멤버를 팀장으로 변경
        ProjectMember updatedNewLeader = ProjectMember.builder()
                .id(newLeaderMember.getId())
                .project(newLeaderMember.getProject())
                .userId(newLeaderMember.getUserId())
                .user(newLeaderMember.getUser())
                .role(MemberRole.LEADER)
                .position(newLeaderMember.getPosition())
                .projectStacks(newLeaderMember.getProjectStacks())
                .build();
        projectMemberRepository.save(updatedNewLeader);

        // 프로젝트의 소유자(ownerId) 변경
        project.setOwnerId(newLeaderUserId);
        User newOwner = userRepository.findById(newLeaderUserId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + newLeaderUserId));
        project.setOwner(newOwner);
        projectRepository.save(project);

        log.info("프로젝트(ID: {})의 팀장 권한을 {}에서 {}로 위임했습니다.", 
                projectId, 
                currentLeader != null ? currentLeader.getUserId() : "none", 
                newLeaderUserId);
    }

    @Override
    @Transactional
    public void updateProjectStatus(Long projectId, String uid, ProjectStatusUpdateRequest requestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트를 찾을 수 없습니다: " + projectId));
        
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + uid));
        
        // 팀장 권한 확인
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, uid)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트의 멤버가 아닙니다: " + uid));
        
        if (member.getRole() != MemberRole.LEADER) {
            throw new IllegalArgumentException("프로젝트 상태 변경은 팀장만 가능합니다.");
        }
        
        // Status enum 검증 및 변경
        Status newStatus = EnumValidator.parseEnum(Status.class, requestDto.getStatus(), "status");
        project.setStatus(newStatus);
        
        log.info("프로젝트(ID: {}) 상태를 {}로 변경했습니다. 변경자: {}", 
                projectId, newStatus.getDisplayName(), uid);
    }
}