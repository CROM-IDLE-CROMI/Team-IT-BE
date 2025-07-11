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

    @PostMapping // HTTP POST 요청 처리
    public ResponseEntity<Void> createTeam(@RequestBody @Valid CreateTeamRequestDto requestDto) {
        teamService.createTeam(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
        // 상태 코드만 반환, 응답 body는 생략
    }
}