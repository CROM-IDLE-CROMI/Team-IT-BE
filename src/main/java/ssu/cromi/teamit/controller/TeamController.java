// HTTP 요청 및 응답 처리
// DTO 수신, 서비스 호출

package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.dto.CreateTeamRequestDto;
import ssu.cromi.teamit.service.TeamService;

@RestController // JSON으로 데이터 처리
@RequestMapping("/v1/teams") // API path 설정
@RequiredArgsConstructor // 의존성 주입 처리

// <팀원 모집> @Method POST
public class TeamController {
    private final TeamService teamService;

    /* @PostMapping
    public ResponseEntity<Void> createTeam(@Valid @RequestBody CreateTeamRequestDto requestDto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUserId();
        teamService.createTeam(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    } */

}