package ssu.cromi.teamit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase; // ★ import 문 추가
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit; // ★ import 문 추가
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.teamup.CreateTeamRequestDto;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.service.teamup.TeamService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ★ 실제 DB 사용 설정
class TeamServiceIntegrationTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("팀 생성 요청 시 Project와 ProjectMember가 DB에 성공적으로 저장된다")
    @Commit // ★ DB에 영구 저장 설정
    void createTeam_Success() {
        // given
        final String userId = "test-user-123";
        userRepository.save(User.builder().uid(userId).nickName("일반테스터").email("test@example.com")
                .password("password").birthday(20030927).point(0L).build());

        List<String> questions = List.of("질문1", "질문2", "질문3");

        CreateTeamRequestDto requestDto = CreateTeamRequestDto.builder()
                .memberNum(4).validFrom(LocalDateTime.now().plusDays(1)).validTo(LocalDateTime.now().plusDays(10))
                .platform("APP").recruitPositions(List.of("BACKEND", "FRONTEND")).requireStack(List.of("Java", "Spring", "React"))
                .projectName("TeamIt 프로젝트").category("CONTEST").startDate(LocalDateTime.now().plusDays(11))
                .endDate(LocalDateTime.now().plusMonths(3)).expectedStartDate(LocalDateTime.now().plusDays(11))
                .creatorPosition("BACKEND").title("최고의 팀원을 모집합니다!").projectStatus("IDEA")
                .ideaExplain("세상을 바꿀 멋진 아이디어입니다.").meetingApproach("ONLINE").locations(List.of("서울", "경기"))
                .minRequest("열정만 있으면 됩니다.").applicantQuestions(questions).build();

        // when
        Long newProjectId = teamService.createTeam(requestDto, userId);

        // then
        assertThat(newProjectId).isNotNull();
    }

    @Test
    @DisplayName("ETC 옵션 선택 시 상세 설명(Detail) 필드가 DB에 성공적으로 저장된다")
    @Commit // ★ DB에 영구 저장 설정
    void createTeam_With_ETC_Options_Success() {
        // given
        final String userId = "etc-test-user";
        userRepository.save(User.builder().uid(userId).nickName("ETC테스터").email("etc@example.com")
                .password("password").birthday(20010101).point(0L).build());

        List<String> recruitDetails = List.of("그로스 해커", "데이터 분석가");

        CreateTeamRequestDto requestDto = CreateTeamRequestDto.builder()
                .memberNum(5).validFrom(LocalDateTime.now().plusDays(1)).validTo(LocalDateTime.now().plusDays(10))
                .platform("ETC").platformDetail("VR/AR")
                .recruitPositions(List.of("BACKEND", "ETC")).recruitDetail(recruitDetails)
                .requireStack(List.of("Unity", "C#")).projectName("ETC 테스트 프로젝트").category("ETC")
                .categoryDetail("메타버스").startDate(LocalDateTime.now().plusDays(11)).endDate(LocalDateTime.now().plusMonths(6))
                .expectedStartDate(LocalDateTime.now().plusDays(11)).creatorPosition("BACKEND")
                .title("ETC 프로젝트 팀원을 모집합니다").projectStatus("ETC").statusDetail("정부 과제 준비중")
                .ideaExplain("가상현실 플랫폼 개발 프로젝트입니다.").meetingApproach("ONLINE").locations(List.of("온라인"))
                .minRequest("관련 경험자 우대").applicantQuestions(List.of("가장 인상 깊었던 VR 경험은?")).build();

        // when
        Long newProjectId = teamService.createTeam(requestDto, userId);

        // then
        assertThat(newProjectId).isNotNull();
    }
}