package ssu.cromi.teamit.service.teamup;

import ssu.cromi.teamit.DTO.teamup.CreateTeamRequestDto;

public interface TeamService {
    Long createTeam(CreateTeamRequestDto requestDto, String userId);
}
