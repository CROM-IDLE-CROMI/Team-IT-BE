package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.Category;
import ssu.cromi.teamit.entity.enums.Platform;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ProjectDetailServiceImpl implements ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional // 조회수 증가 때문에 readOnly=false 처리
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        // 조회수 증가 (벌크 업데이트 방식)
        int updated = projectRepository.incrementViewCount(projectId);
        if (updated == 0) {
            throw new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다.");
        }

        // 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        // 작성자 정보 조회
        User user = userRepository.findById(project.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        // 3. 응답 DTO 조립
        return ProjectDetailResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .projectName(project.getProjectName())
                .creatorId(project.getCreatorId())
                .createdAt(project.getCreatedAt())
                .memberNum(project.getMemberNum())
                .validFrom(project.getValidFrom())
                .validTo(project.getValidTo())
                .platform(project.getPlatform().name())
                .platformDetail(
                        project.getPlatform() == Platform.ETC ? project.getPlatformDetail() : null)
                .recruitPositions(project.getRecruitPositions())
                .requireStack(project.getRequireStack())
                .category(project.getCategory().name())
                .categoryDetail(
                        project.getCategory() == Category.ETC ? project.getCategoryDetail() : null)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .expectedStartDate(project.getExpectedStartDate())
                .projectStatus(project.getStatus().name())
                .statusDetail(
                        project.getStatus() == Status.ETC ? project.getStatusDetail() : null)
                .ideaExplain(project.getIdeaExplain())
                .meetingApproach(project.getMeetingApproach().name())
                .locations(project.getLocations())
                .minRequest(project.getMinRequest())
                .applicantQuestions(project.getApplicantQuestions())
                .viewCount(project.getViewCount())

                .creatorId(project.getCreatorId())
                .creatorNickname(user.getNickName())
                .creatorProfileImageUrl(user.getProfileImg())
                .build();
    }
}
