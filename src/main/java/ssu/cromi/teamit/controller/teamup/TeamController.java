package ssu.cromi.teamit.controller.teamup;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import ssu.cromi.teamit.DTO.CreateTeamRequestDto;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.teamup.TeamService;
import ssu.cromi.teamit.response.ApiResponse;

@RestController
@RequestMapping("/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createTeam(
            @Valid @RequestBody CreateTeamRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        Long projectId = teamService.createTeam(requestDto, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(2006, "프로젝트가 성공적으로 등록되었습니다.",
                        projectId)
        );
    }

}
