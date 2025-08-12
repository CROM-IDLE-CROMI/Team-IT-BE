package ssu.cromi.teamit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.service.findproject.ProjectDetailService;
import ssu.cromi.teamit.entity.enums.Category;
import ssu.cromi.teamit.entity.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProjectDetailServiceIntegrationTest {

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;
    private Project savedProject;

    // 각 테스트가 실행되기 전에 테스트용 데이터를 미리 저장
    @BeforeEach
    void setUp() {
        // 1. 테스트용 User 생성 및 저장
        User user = User.builder()
                .uid("detail-test-user")
                .nickName("상세페이지테스터")
                .email("detail@example.com")
                .password("password")
                .birthday(19990101)
                .point(100L)
                .build();
        savedUser = userRepository.save(user);

        // 2. 테스트용 Project 생성 및 저장
        Project project = Project.builder()
                .creatorId(savedUser.getUid())
                .ownerId(savedUser.getUid())
                .title("상세 조회 테스트용 프로젝트")
                .projectName("Detail Project")
                .memberNum(3)
                .platform(Platform.WEB)
                .category(Category.CONTEST)
                .projectStatus(ProjectStatus.IDEA)
                .meetingApproach(MeetingApproach.OFFLINE)
                .recruitPositions(List.of("BACKEND"))
                .requireStack(List.of("Java"))
                .locations(List.of("서울"))
                .ideaExplain("테스트용 본문입니다.")
                .minRequest("성실함")
                .applicantQuestions(List.of("지원동기는?"))
                .validFrom(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(10))
                .startDate(LocalDateTime.now().plusDays(11))
                .endDate(LocalDateTime.now().plusMonths(2))
                .expectedStartDate(LocalDateTime.now().plusDays(11))
                .build();
        savedProject = projectRepository.save(project);
    }

    @Test
    @DisplayName("프로젝트 상세 조회 시 DTO가 올바르게 반환되고 조회수가 1 증가한다")
    @Commit
    void getProjectDetail_Success() {
        // given: setUp()에서 데이터가 이미 준비된 상태
        Long projectId = savedProject.getId();
        long initialViewCount = savedProject.getViewCount();

        // when: 서비스 메서드 호출
        ProjectDetailResponseDto resultDto = projectDetailService.getProjectDetail(projectId);

        // then: 결과 검증
        // 1. DTO 내용 검증
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getProjectId()).isEqualTo(projectId);
        assertThat(resultDto.getTitle()).isEqualTo("상세 조회 테스트용 프로젝트");
        assertThat(resultDto.getCreatorNickname()).isEqualTo("상세페이지테스터");
        assertThat(resultDto.getPlatform()).isEqualTo(Platform.WEB.name());

        // 2. 조회수 증가 검증
        Project projectAfterView = projectRepository.findById(projectId).get();
        assertThat(projectAfterView.getViewCount()).isEqualTo(initialViewCount + 1);
        // DTO에 담긴 조회수도 증가된 값인지 확인
        assertThat(resultDto.getViewCount()).isEqualTo(initialViewCount + 1);

        System.out.println("✅ 테스트 성공: 상세 조회 DTO가 올바르게 반환되었고, 조회수가 1 증가했습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 프로젝트 ID로 조회 시 예외가 발생한다")
    void getProjectDetail_Fail_ProjectNotFound() {
        // given: 존재하지 않는 ID
        Long nonExistentProjectId = 9999L;

        // when & then: 예외 발생 검증
        assertThrows(ProjectNotFoundException.class, () -> {
            projectDetailService.getProjectDetail(nonExistentProjectId);
        });

        System.out.println("✅ 테스트 성공: 존재하지 않는 프로젝트 조회 시 ProjectNotFoundException이 정상적으로 발생했습니다.");
    }
}