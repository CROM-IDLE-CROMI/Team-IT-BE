package ssu.cromi.teamit.service.teamup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import ssu.cromi.teamit.DTO.teamup.CreateTeamRequestDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.exception.InvalidEnumValueException;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;

import static ssu.cromi.teamit.util.EnumValidator.parseEnum;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createTeam(CreateTeamRequestDto dto, String userId) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));

        Platform       platform       = parseEnum(Platform.class,       dto.getPlatform(),        "platform");
        Category       category       = parseEnum(Category.class,       dto.getCategory(),        "category");
        ProjectStatus  projectStatus  = parseEnum(ProjectStatus.class,  dto.getProjectStatus(),   "projectStatus");
        MeetingApproach approach      = parseEnum(MeetingApproach.class,dto.getMeetingApproach(), "meetingApproach");

        if (platform == Platform.ETC && StringUtils.isBlank(dto.getPlatformDetail())) {
            throw new InvalidEnumValueException("platformDetail", "기타 플랫폼 상세 내용을 입력하세요.");
        }
        if (category == Category.ETC && StringUtils.isBlank(dto.getCategoryDetail())) {
            throw new InvalidEnumValueException("categoryDetail", "기타 카테고리 상세 내용을 입력하세요.");
        }
        if (projectStatus == ProjectStatus.ETC && StringUtils.isBlank(dto.getStatusDetail())) {
            throw new InvalidEnumValueException("statusDetail", "기타 상태 상세 내용을 입력하세요.");
        }

        List<String> recruitPositions = dto.getRecruitPositions().stream()
                .map(pos -> {
                    Position parsed = parseEnum(Position.class, pos, "recruitPositions");
                    return parsed.name();
                })
                .collect(Collectors.toList());

        if (recruitPositions.contains(Position.ETC.name())
                && (dto.getRecruitDetail() == null || dto.getRecruitDetail().isEmpty())) {
            throw new InvalidEnumValueException("recruitDetail", "기타 모집 직군 상세 내용을 입력하세요.");
        }


        // 3) Project 엔티티 빌드
        Project project = Project.builder()
                .creatorId(userId)
                .owner(owner)
                .memberNum(dto.getMemberNum())
                .validFrom(dto.getValidFrom())
                .validTo(dto.getValidTo())
                .platform(platform)
                .platformDetail(dto.getPlatformDetail())
                .recruitPositions(recruitPositions)
                .recruitDetail(dto.getRecruitDetail())
                .requireStack(dto.getRequireStack())
                .projectName(dto.getProjectName())
                .category(category)
                .categoryDetail(dto.getCategoryDetail())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .expectedStartDate(dto.getExpectedStartDate())
                .title(dto.getTitle())
                .projectStatus(projectStatus)
                .statusDetail(dto.getStatusDetail())
                .ideaExplain(dto.getIdeaExplain())
                .meetingApproach(approach)
                .locations(dto.getLocations())
                .minRequest(dto.getMinRequest())
                .applicantQuestions(dto.getApplicantQuestions())
                .writingStatus(WritingStatus.COMPLETED)
                .progress(0)
                .viewCount(0)
                .build();

        Project saved = projectRepository.save(project);

        Position leaderPos = parseEnum(Position.class, dto.getCreatorPosition(), "creatorPosition");

        ProjectMember leader = ProjectMember.builder()
                .project(saved)
                .user(owner)
                .role(MemberRole.LEADER)
                .position(leaderPos)
                .build();

        projectMemberRepository.save(leader);

        return saved.getId();
    }
}
