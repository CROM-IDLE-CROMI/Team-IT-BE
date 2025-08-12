package ssu.cromi.teamit.controller.findproject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ssu.cromi.teamit.DTO.findproject.ProjectApplicationRequestDto;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.findproject.ProjectApplicationService;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectApplicationController {

    private final ProjectApplicationService projectApplicationService;

    @PostMapping("/{projectId}/apply")
    public ResponseEntity<Void> applyToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectApplicationRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String applicantId = userDetails.getUid(); // 로그인된 사용자 ID
        projectApplicationService.applyToProject(dto, applicantId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
