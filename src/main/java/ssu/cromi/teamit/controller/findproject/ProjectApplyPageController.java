package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssu.cromi.teamit.DTO.findproject.ProjectApplyPageResponseDto;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.findproject.ProjectApplyPageService;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor

public class ProjectApplyPageController {
    private final ProjectApplyPageService projectApplyPageService;

    @GetMapping("/{projectId}/submission")
    public ResponseEntity<ProjectApplyPageResponseDto> getApplicationPage(@PathVariable Long projectId) {
        ProjectApplyPageResponseDto dto = projectApplyPageService.getApplicationPage(projectId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{projectId}/submission/my")
    public ResponseEntity<ProjectApplyPageResponseDto> getMyApplicationPage(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProjectApplyPageResponseDto dto = projectApplyPageService
                .getApplicationPageWithUserData(projectId, userDetails.getUid());
        return ResponseEntity.ok(dto);
    }
}
