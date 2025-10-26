package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssu.cromi.teamit.DTO.common.PageResponse;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.service.findproject.ProjectListService;

@RestController
@RequestMapping("/v1/projects") // 예시 경로
@RequiredArgsConstructor
public class ProjectListController {

    private final ProjectListService projectListService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProjectSummaryResponseDto>>> getProjectList(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProjectSummaryResponseDto> projectPage = projectListService.getProjectList(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "프로젝트 목록이 성공적으로 조회되었습니다.", PageResponse.of(projectPage))
        );
    }
}