package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;


import ssu.cromi.teamit.dto.CreateTeamRequestDto;
// import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.TeamService;
import ssu.cromi.teamit.response.ApiResponse;

@RestController
@RequestMapping("/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /* @PostMapping
    public ResponseEntity<ApiResponse<?>> createTeam(
            @Valid @RequestBody CreateTeamRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        Long projectId = teamService.createTeam(requestDto, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(2006, "프로젝트가 성공적으로 등록되었습니다.",
                        projectId)
        );
    } */

    @PostMapping
    public ResponseEntity<?> createTeam(@Valid @RequestBody CreateTeamRequestDto requestDto) {
        // 테스트용 userId 직접 지정
        String fakeUserId = "TestUser123";
        teamService.createTeam(requestDto, fakeUserId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
