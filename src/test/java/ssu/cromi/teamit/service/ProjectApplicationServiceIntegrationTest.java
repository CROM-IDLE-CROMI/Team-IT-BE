package ssu.cromi.teamit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectApplicationRequestDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.findproject.ProjectApplicationRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.service.findproject.ProjectApplicationService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ★ 실제 DB 사용
class ProjectApplicationServiceIntegrationTest {

    @Autowired
    private ProjectApplicationService projectApplicationService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectApplicationRepository projectApplicationRepository;

    private User applicant;
    private Project projectToApply;

    @BeforeEach
    void setUp() {
        applicant = userRepository.save(User.builder()
                .uid("applicant-user")
                .nickName("지원자")
                .email("applicant@example.com")
                .password("password")
                .birthday(20000101)
                .point(100L)
                .build());

        User creator = userRepository.save(User.builder()
                .uid("creator-user")
                .nickName("프로젝트생성자")
                .email("creator@example.com")
                .password("password")
                .birthday(19980101)
                .point(100L)
                .build());

        projectToApply = projectRepository.save(Project.builder()
                .creatorId(creator.getUid())
                .ownerId(creator.getUid())
                .title("테스트 프로젝트")
                .projectName("Test Project")
                .memberNum(3).platform(Platform.WEB).category(Category.CONTEST).status(Status.RECRUITING)
                .projectStatus(ProjectStatus.IN_PROGRESS).meetingApproach(MeetingApproach.ONLINE)
                .recruitPositions(List.of("BACKEND")).requireStack(List.of("Java"))
                .locations(List.of("서울")).ideaExplain("내용").minRequest("요건")
                .applicantQuestions(List.of("질문1", "질문2"))
                .validFrom(LocalDateTime.now()).validTo(LocalDateTime.now().plusDays(1))
                .startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusMonths(1))
                .expectedStartDate(LocalDateTime.now())
                .build());
    }

    @Test
    @DisplayName("프로젝트 지원 시 지원서가 DB에 성공적으로 저장된다")
    @Commit // ★ DB에 영구 저장
    void applyToProject_Success() {
        // given
        ProjectApplicationRequestDto requestDto = ProjectApplicationRequestDto.builder()
                .title("열정적인 백엔드 개발자입니다!")
                .position("BACKEND")
                .motivation("귀사의 프로젝트와 함께 성장하고 싶습니다.")
                .answers(List.of("답변1입니다.", "답변2입니다."))
                .requirements(true) // ★ 필드 이름이 'requirements'가 아니라 'meetsRequirements' 입니다.
                .build();

        // when
        projectApplicationService.applyToProject(requestDto, applicant.getUid(), projectToApply.getId());

        // then
        List<ProjectApplication> applications = projectApplicationRepository.findAll();
        assertThat(applications).hasSize(1);

        ProjectApplication savedApplication = applications.get(0);
        assertThat(savedApplication.getApplicantId()).isEqualTo(applicant.getUid());
        assertThat(savedApplication.getProject().getId()).isEqualTo(projectToApply.getId());
        assertThat(savedApplication.getTitle()).isEqualTo("열정적인 백엔드 개발자입니다!");
        assertThat(savedApplication.getRequirements()).isTrue(); // ★ 검증 필드 이름도 수정
    }

    @Test
    @DisplayName("존재하지 않는 프로젝트에 지원 시 예외가 발생한다")
    void applyToProject_Fail_ProjectNotFound() {
        // given
        Long nonExistentProjectId = 9999L;
        ProjectApplicationRequestDto requestDto = ProjectApplicationRequestDto.builder()
                .title("제목").position("직군").motivation("동기").answers(List.of("답변"))
                .requirements(true) // ★ 필드 이름이 'requirements'가 아니라 'meetsRequirements' 입니다.
                .build();

        // when & then
        assertThrows(ProjectNotFoundException.class, () -> {
            projectApplicationService.applyToProject(requestDto, applicant.getUid(), nonExistentProjectId);
        });
    }
}