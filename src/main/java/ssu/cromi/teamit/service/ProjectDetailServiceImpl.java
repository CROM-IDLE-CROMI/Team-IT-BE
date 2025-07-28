
package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.dto.ProjectDetailResponseDto;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.entity.User;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.repository.ProjectRepository;
import ssu.cromi.teamit.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        // 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        // 작성자 정보 조회
        User user = userRepository.findById(project.getCreaterId())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        return ProjectDetailResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .projectName(project.getProjectName())
                .creatorId(project.getCreaterId())
                .createdAt(project.getCreatedAt())
                .memberNum(project.getMemberNum())
                .validFrom(project.getValidFrom())
                .validTo(project.getValidTo())
                .platform(project.getPlatform().name())
                .platformDetail(project.getPlatformDetail())
                .recruitPositions(project.getRecruitPositions())
                .requireStack(project.getRequireStack())
                .category(project.getCategory().name())
                .categoryDetail(project.getCategoryDetail())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .expectedStartDate(project.getExpectedStartDate())
                .projectStatus(project.getProjectStatus().name())
                .statusDetail(project.getStatusDetail())
                .ideaExplain(project.getExplain())
                .meetingApproach(project.getMeetingApproach().name())
                .locations(project.getLocations())
                .minRequest(project.getMinRequest())
                .applicantQuestions(project.getApplicantQuestions())
                .uid(user.getUid())
                .nickName(user.getNickname())
                .creatorIntro(user.getIntroduce())
                .creatorProfileImageUrl(user.getProfileImageUrl())
                .creatorTechStack(user.getTechStack()) // List<String>
                .build();
    }
}
