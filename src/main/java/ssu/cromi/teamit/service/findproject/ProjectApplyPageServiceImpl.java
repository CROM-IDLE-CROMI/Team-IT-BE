package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectApplyPageResponseDto;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApplyPageServiceImpl implements ProjectApplyPageService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ProjectApplyPageResponseDto getApplicationPage(Long projectId) {
        // 1. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        // 2. 작성자 정보 조회
        User user = userRepository.findById(project.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("작성자 정보가 존재하지 않습니다."));

        // 3. projectDetail DTO 구성
        ProjectDetailResponseDto projectDetail = ProjectDetailResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .projectName(project.getProjectName())
                .createdAt(project.getCreatedAt())
                .viewCount(project.getViewCount())
                .memberNum(project.getMemberNum())
                .validFrom(project.getValidFrom())
                .validTo(project.getValidTo())
                .platform(project.getPlatform().name())
                .platformDetail(project.getPlatformDetail())
                .recruitPositions(project.getRecruitPositions())
                .recruitDetail(project.getRecruitDetail())
                .requireStack(project.getRequireStack())
                .category(project.getCategory().name())
                .categoryDetail(project.getCategoryDetail())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .expectedStartDate(project.getExpectedStartDate())
                .projectStatus(project.getProjectStatus().name())
                .statusDetail(project.getStatusDetail())
                .ideaExplain(project.getIdeaExplain())
                .meetingApproach(project.getMeetingApproach().name())
                .locations(project.getLocations())
                .minRequest(project.getMinRequest())
                .applicantQuestions(project.getApplicantQuestions())
                .creatorId(user.getUid())
                .creatorNickname(user.getNickName())
                .creatorProfileImageUrl(user.getProfileImg())
                .build();

        // 4. 질문 수만큼 빈 answers 초기화
        List<String> emptyAnswers = project.getApplicantQuestions() != null
                ? project.getApplicantQuestions().stream().map(q -> "").toList()
                : Collections.emptyList();

        // 5. 응답 DTO 구성
        return ProjectApplyPageResponseDto.builder()
                .projectDetail(projectDetail)
                .title("")
                .position("")
                .motivation("")
                .answers(emptyAnswers)
                .build();
    }
}

