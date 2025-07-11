package ssu.cromi.teamit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssu.cromi.teamit.dto.CreateTeamRequestDto;
import ssu.cromi.teamit.entity.Project;
import ssu.cromi.teamit.repository.TeamRepository;

@Service
@RequiredArgsConstructor

public class TeamService {
    private final TeamRepository teamRepository;

    public void createTeam(CreateTeamRequestDto requestDto) {
        // Controller에서 받은 DTO 기반으로 팀 모집글 생성
        // Dto → Entity
        /* Project team = Project.builder() // Entity 객체 생성
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .recruitField(requestDto.getRecruitField())
                .contactLink(requestDto.getContactLink())
                .deadline(requestDto.getDeadline())
                .build();

        teamRepository.save(team); // DB 저장 */
    }
}