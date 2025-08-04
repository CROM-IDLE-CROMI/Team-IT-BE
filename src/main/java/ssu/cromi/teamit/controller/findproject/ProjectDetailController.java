package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.service.findproject.ProjectDetailService;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectDetailController {

    private final ProjectDetailService projectDetailService;

    /**
     * 프로젝트 상세 페이지 조회
     * @param projectId 조회할 프로젝트 ID
     * @return 상세 정보 DTO
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponseDto> getProjectDetail(@PathVariable Long projectId) {
        ProjectDetailResponseDto detailDto = projectDetailService.getProjectDetail(projectId);
        return ResponseEntity.ok(detailDto);
    }
}
