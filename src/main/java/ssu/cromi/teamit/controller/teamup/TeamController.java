package ssu.cromi.teamit.controller.teamup;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ssu.cromi.teamit.DTO.teamup.CreateTeamRequestDto;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.teamup.TeamService;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.service.impl.UserServiceImpl;
import ssu.cromi.teamit.domain.User;

@RestController
@RequestMapping("/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDetailResponseDto>> createTeam(
            @Valid @RequestBody CreateTeamRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        User creator = userServiceImpl.findByUid(userId);

        Project createdProject = teamService.createTeam(requestDto, userId);

        ProjectDetailResponseDto responseDto = ProjectDetailResponseDto.from(createdProject, creator);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(2006, "프로젝트가 성공적으로 등록되었습니다.", responseDto)
        );
    }

}
