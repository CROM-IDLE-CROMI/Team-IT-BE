package ssu.cromi.teamit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ssu.cromi.teamit.service.teamup.TeamService;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.teamup.CreateTeamRequestDto;
import ssu.cromi.teamit.entity.enums.MemberRole;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class TeamServiceIntegrationTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired // ★ UserRepository 주입 추가
    private UserRepository userRepository;

    @Test
    @DisplayName("팀 생성 요청 시 Project와 ProjectMember가 DB에 성공적으로 저장된다")
    void createTeam_Success() {
        // given: 테스트를 위한 준비 단계
        final String userId = "test-user-123";
// ★★★ 시작: 테스트용 User 생성 및 저장 ★★★
        User testUser = User.builder()
                .uid(userId)
                .nickName("테스트유저")
                .email("test@example.com")
                .password("password") // 실제 User 엔티티에 맞게 필요한 필드를 채워주세요.
                .birthday(20030927)
                .point(0L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .emailVerified(false)
                .build();
        userRepository.save(testUser);
        // ★★★ 종료: 테스트용 User 생성 및 저장 ★★★

        LocalDateTime now = LocalDateTime.now();

        // 3개의 질문이 담긴 리스트
        List<String> questions = List.of(
                "가장 자신있는 기술은 무엇인가요?",
                "프로젝트를 통해 가장 얻고 싶은 것은 무엇인가요?",
                "팀워크에서 가장 중요하다고 생각하는 가치는 무엇입니까?"
        );

        // 클라이언트가 보낼 요청 DTO를 생성
        CreateTeamRequestDto requestDto = CreateTeamRequestDto.builder()
                .memberNum(4)
                .validFrom(now.plusDays(1))
                .validTo(now.plusDays(10))
                .platform("APP")
                .recruitPositions(List.of("BACKEND", "FRONTEND"))
                .requireStack(List.of("Java", "Spring", "React"))
                .projectName("TeamIt 프로젝트")
                .category("CONTEST")
                .startDate(now.plusDays(11))
                .endDate(now.plusMonths(3))
                .expectedStartDate(now.plusDays(11))
                .creatorPosition("BACKEND")
                .title("최고의 팀원을 모집합니다!")
                .projectStatus("IDEA")
                .ideaExplain("세상을 바꿀 멋진 아이디어입니다.")
                .meetingApproach("ONLINE")
                .locations(List.of("서울", "경기"))
                .minRequest("열정만 있으면 됩니다.")
                .applicantQuestions(questions) // ★ 미리 만들어 둔 'questions' 변수를 사용하도록 수정
                .build();

        // when: 실제 테스트 대상 메서드 실행
        Long newProjectId = teamService.createTeam(requestDto, userId);


        // then: 실행 결과 검증
        assertThat(newProjectId).isNotNull();

        Optional<Project> savedProjectOpt = projectRepository.findById(newProjectId);
        assertThat(savedProjectOpt).isPresent();

        Project savedProject = savedProjectOpt.get();
        assertThat(savedProject.getCreatorId()).isEqualTo(userId);
        assertThat(savedProject.getOwnerId()).isEqualTo(userId);
        assertThat(savedProject.getProjectName()).isEqualTo("TeamIt 프로젝트");
        assertThat(savedProject.getMemberNum()).isEqualTo(4);
        assertThat(savedProject.getRequireStack()).containsExactly("Java", "Spring", "React");
        assertThat(savedProject.getRecruitPositions()).contains("BACKEND", "FRONTEND");

        // DB에 저장된 질문 목록이 우리가 넣은 3개의 질문과 정확히 일치하는지 확인
        assertThat(savedProject.getApplicantQuestions()).containsExactly(
                "가장 자신있는 기술은 무엇인가요?",
                "프로젝트를 통해 가장 얻고 싶은 것은 무엇인가요?",
                "팀워크에서 가장 중요하다고 생각하는 가치는 무엇입니까?"
        );

        Optional<ProjectMember> savedMemberOpt = projectMemberRepository.findByProjectId(newProjectId);
        assertThat(savedMemberOpt).isPresent();

        ProjectMember leader = savedMemberOpt.get();
        assertThat(leader.getUserId()).isEqualTo(userId);
        assertThat(leader.getRole()).isEqualTo(MemberRole.LEADER);
        assertThat(leader.getPosition().name()).isEqualTo("BACKEND");
        assertThat(leader.getProject().getId()).isEqualTo(newProjectId);

        System.out.println("✅ 테스트 성공: Project 및 ProjectMember가 DB에 올바르게 저장되었습니다.");
    }
}