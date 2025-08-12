package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ssu.cromi.teamit.DTO.findproject.ProjectApplyPageResponseDto;
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
}
