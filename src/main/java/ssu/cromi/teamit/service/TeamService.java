package ssu.cromi.teamit.service;

import ssu.cromi.teamit.dto.CreateTeamRequestDto;

public interface TeamService {
    Long createTeam(CreateTeamRequestDto requestDto, String userId);
}
