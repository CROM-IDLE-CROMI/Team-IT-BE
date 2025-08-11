package ssu.cromi.teamit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectApplyPageResponseDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ssu.cromi.teamit.service.findproject.ProjectApplyPageService;

@SpringBootTest
@Transactional
class ProjectApplyPageServiceIntegrationTest {

    @Autowired
    private ProjectApplyPageService projectApplyPageService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;
    private Project savedProject;

    @BeforeEach
    void setUp() {
        // 1. 테스트용 User 생성 및 저장
        User user = User.builder()
                .uid("apply-test-user")
                .nickName("지원서테스터")
                .email("apply@example.com")
                .password("password")
                .birthday(20020101)
                .point(200L)
                .build();
        savedUser = userRepository.save(user);

        // 2. 테스트용 Project 생성 및 저장 (모든 필수 필드 포함)
        Project project = Project.builder()
                .creatorId(savedUser.getUid())
                .ownerId(savedUser.getUid())
                .title("지원 페이지 테스트 프로젝트")
                .projectName("Apply Project")
                .applicantQuestions(List.of("질문1", "질문2", "질문3"))
                .memberNum(3)
                .platform(Platform.APP)
                .category(Category.CONTEST)
                .status(Status.RECRUITING) // 모집 상태 필드
                .projectStatus(ProjectStatus.DESIGN) // ★ 방금 주신 Enum을 사용한 프로젝트 진행 상태 필드
                .meetingApproach(MeetingApproach.ONLINE)
                .recruitPositions(List.of("DESIGNER"))
                .requireStack(List.of("Figma"))
                .locations(List.of("온라인"))
                .ideaExplain("테스트용 본문입니다.")
                .minRequest("책임감")
                .validFrom(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(10))
                .startDate(LocalDateTime.now().plusDays(11))
                .endDate(LocalDateTime.now().plusMonths(2))
                .expectedStartDate(LocalDateTime.now().plusDays(11))
                .build(); // ★ 빌더는 맨 마지막에 한 번만 호출
        savedProject = projectRepository.save(project);
    }

    @Test
    @DisplayName("프로젝트 지원 페이지 조회 시 상세 정보와 빈 답변 목록이 올바르게 반환된다")
    void getApplicationPage_Success() {
        // given: setUp()에서 데이터가 이미 준비된 상태
        Long projectId = savedProject.getId();

        // when: 서비스 메서드 호출
        ProjectApplyPageResponseDto resultDto = projectApplyPageService.getApplicationPage(projectId);

        // then: 결과 검증
        // 1. DTO가 null이 아닌지 확인
        assertThat(resultDto).isNotNull();

        // 2. ProjectDetail 정보가 올바르게 담겼는지 확인
        assertThat(resultDto.getProjectDetail()).isNotNull();
        assertThat(resultDto.getProjectDetail().getProjectId()).isEqualTo(projectId);
        assertThat(resultDto.getProjectDetail().getTitle()).isEqualTo("지원 페이지 테스트 프로젝트");
        assertThat(resultDto.getProjectDetail().getCreatorNickname()).isEqualTo("지원서테스터");
        assertThat(resultDto.getProjectDetail().getApplicantQuestions()).hasSize(3);

        // 3. 지원서 양식의 초기값이 올바르게 설정되었는지 확인
        assertThat(resultDto.getTitle()).isEmpty();
        assertThat(resultDto.getPosition()).isEmpty();
        assertThat(resultDto.getMotivation()).isEmpty();

        // 4. 질문 개수만큼 빈 답변 목록이 생성되었는지 확인
        assertThat(resultDto.getAnswers()).isNotNull();
        assertThat(resultDto.getAnswers()).hasSize(3); // 질문이 3개였으므로, 답변 목록의 크기도 3이어야 함
        assertThat(resultDto.getAnswers()).containsOnly(""); // 모든 항목이 빈 문자열("")인지 확인

        System.out.println("✅ 테스트 성공: 지원 페이지 DTO가 올바르게 반환되었습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 프로젝트 ID로 조회 시 예외가 발생한다")
    void getApplicationPage_Fail_ProjectNotFound() {
        // given: 존재하지 않는 ID
        Long nonExistentProjectId = 9999L;

        // when & then: 예외 발생 검증
        assertThrows(ProjectNotFoundException.class, () -> {
            projectApplyPageService.getApplicationPage(nonExistentProjectId);
        });

        System.out.println("✅ 테스트 성공: 존재하지 않는 프로젝트 조회 시 ProjectNotFoundException이 정상적으로 발생했습니다.");
    }
}