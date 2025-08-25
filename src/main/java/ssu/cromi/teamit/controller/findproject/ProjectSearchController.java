package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.service.findproject.ProjectSearchService;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectSearchController {

    private final ProjectSearchService projectSearchService;

    // 프로젝트 다중 조건 검색 API

    @PostMapping("/search")
    public ResponseEntity<Page<ProjectSummaryResponseDto>> searchProjects(
            @RequestBody ProjectSearchRequestDto searchDto,
            @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable
    ) {
        Page<ProjectSummaryResponseDto> results = projectSearchService.searchProjects(searchDto, pageable);
        return ResponseEntity.ok(results);
    }
}