package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectApplyPageResponseDto;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.findproject.ProjectApplicationRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApplyPageServiceImpl implements ProjectApplyPageService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectApplicationRepository projectApplicationRepository;

    @Override
    @Transactional(readOnly = true)
    public ProjectApplyPageResponseDto getApplicationPage(Long projectId) {
        // 1. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다. id=" + projectId));

        // 2. 작성자 정보 조회
        User user = userRepository.findById(project.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("작성자 정보가 존재하지 않습니다. id=" + project.getCreatorId()));

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

    @Override
    @Transactional(readOnly = true)
    public ProjectApplyPageResponseDto getApplicationPageWithUserData(Long projectId, String applicantId) {
        // 1. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        // 2. 작성자 정보 조회
        User user = userRepository.findById(project.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("작성자 정보가 존재하지 않습니다."));

        // 3. 사용자의 지원서 조회
        List<ProjectApplication> applications = projectApplicationRepository
                .findByProjectIdAndApplicantId(projectId, applicantId);

        // 4. projectDetail DTO 구성 (기존과 동일)
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

        // 5. 사용자의 지원서 데이터가 있으면 해당 데이터로, 없으면 빈 값으로 초기화
        String title = "";
        String position = "";
        String motivation = "";
        List<String> answers;

        if (!applications.isEmpty()) {
            // 가장 최근 지원서 사용 (여러 개가 있을 경우)
            ProjectApplication latestApplication = applications.get(0);
            title = latestApplication.getTitle();
            position = latestApplication.getPosition();
            motivation = latestApplication.getMotivation();
            answers = latestApplication.getAnswers();
        } else {
            // 질문 수만큼 빈 answers 초기화
            answers = project.getApplicantQuestions() != null
                    ? project.getApplicantQuestions().stream().map(q -> "").toList()
                    : Collections.emptyList();
        }

        // 6. 응답 DTO 구성
        return ProjectApplyPageResponseDto.builder()
                .projectDetail(projectDetail)
                .title(title)
                .position(position)
                .motivation(motivation)
                .answers(answers)
                .build();
    }
}

