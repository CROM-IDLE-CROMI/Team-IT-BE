package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.CreateTeamRequestDto;

public interface TeamService {
    Long createTeam(CreateTeamRequestDto requestDto, String userId);
}
