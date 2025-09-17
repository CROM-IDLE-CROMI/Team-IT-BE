package ssu.cromi.teamit.service.teamup;

import ssu.cromi.teamit.DTO.teamup.CreateTeamRequestDto;
import ssu.cromi.teamit.entity.teamup.Project;

public interface TeamService {
    Project createTeam(CreateTeamRequestDto requestDto, String userId);
}
