package ssu.cromi.teamit.controller.findproject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ssu.cromi.teamit.DTO.findproject.ProjectApplicationRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectApplicationResponse;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.findproject.ProjectApplicationService;

import ssu.cromi.teamit.service.UserService;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectApplicationController {

    private final ProjectApplicationService projectApplicationService;
    private final UserService userService;

    @PostMapping("/{projectId}/apply")
    public ResponseEntity<ApiResponse<ProjectApplicationResponse>> applyToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectApplicationRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String applicantId = userDetails.getUid();
        ProjectApplication savedApplication = projectApplicationService.applyToProject(dto, applicantId, projectId);
        User applicant = userService.findByUid(applicantId);

        ProjectApplicationResponse responseDto = ProjectApplicationResponse.of(savedApplication, applicant);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(201, "프로젝트 지원이 성공적으로 등록되었습니다.", responseDto)
        );
    }
}
