package ssu.cromi.teamit.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import ssu.cromi.teamit.dto.CreateTeamRequestDto;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.entity.ProjectMember;
import ssu.cromi.teamit.repository.ProjectMemberRepository;
import ssu.cromi.teamit.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Commit  // 실제 DB 반영을 위해 추가
public class TeamServiceSqlTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Test
    public void createTeam_SaveToMySQL_Success() {
        // given
        String userId = "TestUser123";

        CreateTeamRequestDto dto = CreateTeamRequestDto.builder()
                .memberNum(4)
                .validFrom(LocalDateTime.of(2025, 8, 1, 12, 0))
                .validTo(LocalDateTime.of(2025, 8, 15, 12, 0))
                .platform("ETC")
                .platformDetail("기타 설명")
                .recruitPositions(List.of("BACKEND", "FRONTEND"))
                .requireStack(List.of("Java", "Spring Boot"))
                .projectName("teamit")
                .category("CONTEST")
                .startDate(LocalDateTime.of(2025, 8, 10, 12, 0))
                .endDate(LocalDateTime.of(2025, 9, 10, 12, 0))
                .expectedStartDate(LocalDateTime.of(2025, 8, 8, 12, 0))
                .createrPosition("BACKEND")
                .title("도도영과 함께 할 분을 찾습니다")
                .projectStatus("IDEA")
                .ideaExplain("북한 여행 기획 프로젝트입니다.")
                .meetingApproach("OFFLINE")
                .locations(List.of("서울", "평양"))
                .minRequest("FE 경력 2년 이상")
                .applicantQuestions(List.of("가장 좋아하는 언어는?", "협업 경험은?"))
                .build();

        // when
        Long savedId = teamService.createTeam(dto, userId);

        // then
        Project savedProject = teamRepository.findById(savedId).orElse(null);
        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getCreaterId()).isEqualTo(userId);
        assertThat(savedProject.getProjectName()).isEqualTo("teamit");

        List<ProjectMember> members = projectMemberRepository.findAll();
        assertThat(members).anyMatch(m ->
                m.getUserId().equals(userId) &&
                        m.getRole().name().equals("LEADER") &&
                        m.getProject().getId().equals(savedId)
        );
    }
}


