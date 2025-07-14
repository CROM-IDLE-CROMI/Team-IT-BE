package ssu.cromi.teamit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ssu.cromi.teamit.dto.CreateTeamRequestDto;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.repository.TeamRepository;

@SpringBootTest
@Transactional
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void createProjectTest() {
        // given
        CreateTeamRequestDto dto = new CreateTeamRequestDto();
        ReflectionTestUtils.setField(dto, "createrId", "testUser");
        ReflectionTestUtils.setField(dto, "projectName", "Test Project");
        ReflectionTestUtils.setField(dto, "platform", "WEB");         // enum 이름(영어)과 맞춰야 함
        ReflectionTestUtils.setField(dto, "category", "CONTEST");     // enum 이름
        ReflectionTestUtils.setField(dto, "projectStatus", "IN_PROGRESS");
        ReflectionTestUtils.setField(dto, "meetingApproach", "ONLINE");
        ReflectionTestUtils.setField(dto, "title", "테스트 제목");
        ReflectionTestUtils.setField(dto, "ideaExplain", "아이디어 설명");
        ReflectionTestUtils.setField(dto, "minRequest", "자바 가능자");
        ReflectionTestUtils.setField(dto, "questions", "간단한 소개 부탁드립니다.");
        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.setField(dto, "startDate", now);
        ReflectionTestUtils.setField(dto, "endDate", now.plusDays(30));
        ReflectionTestUtils.setField(dto, "validFrom", now);
        ReflectionTestUtils.setField(dto, "validTo", now.plusDays(10));
        ReflectionTestUtils.setField(dto, "memberNum", 3);
        ReflectionTestUtils.setField(dto, "memberPosition", "백엔드");
        ReflectionTestUtils.setField(dto, "requireStack", List.of("Spring", "MySQL"));
        ReflectionTestUtils.setField(dto, "position", List.of("프론트엔드", "디자이너"));
        ReflectionTestUtils.setField(dto, "location", "서울");

        // when
        teamService.createTeam(dto);

        // then
        List<Project> result = teamRepository.findAll();
        assertThat(result).hasSize(1);
        Project p = result.get(0);
        assertThat(p.getCreaterId()).isEqualTo("testUser");
        assertThat(p.getPlatform().name()).isEqualTo("WEB");
    }
}