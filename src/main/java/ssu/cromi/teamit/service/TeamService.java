package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssu.cromi.teamit.dto.CreateTeamRequestDto;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.entity.Category;
import ssu.cromi.teamit.entity.MeetingApproach;
import ssu.cromi.teamit.entity.Platform;
import ssu.cromi.teamit.entity.ProjectStatus;
import ssu.cromi.teamit.repository.TeamRepository;

@Service
@RequiredArgsConstructor

public class TeamService {
    private final TeamRepository teamRepository;

    public void createTeam(CreateTeamRequestDto requestDto) {
        // Controller에서 받은 DTO 기반으로 팀 모집글 생성
        // Dto → Entity
        Project project = Project.builder()
                .createrId(requestDto.getCreaterId())
                .memberNum(requestDto.getMemberNum())
                .memberPosition(requestDto.getMemberPosition())
                .requireStack(requestDto.getRequireStack())
                .validFrom(requestDto.getValidFrom())
                .validTo(requestDto.getValidTo())
                .platform(Platform.valueOf(requestDto.getPlatform()))
                .category(Category.valueOf(requestDto.getCategory()))
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .projectStatus(ProjectStatus.valueOf(requestDto.getProjectStatus()))
                .title(requestDto.getTitle())
                .ideaExplain(requestDto.getIdeaExplain())
                .projectName(requestDto.getProjectName())
                .meetingApproach(MeetingApproach.valueOf(requestDto.getMeetingApproach()))
                // ** .location(requestDto.getLocation())
                .minRequest(requestDto.getMinRequest())
                .questions(requestDto.getQuestions())
                .build();


        teamRepository.save(project); // DB 저장
    }
}