/* package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.service.findproject.ProjectSearchService;
import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectSearchController {
    private final ProjectSearchService projectSearchService;

    // 제목으로 검색
    @GetMapping("/search/title")
    public ResponseEntity<List<ProjectDetailResponseDto>> searchByTitle(
            @RequestParam("keyword") String keyword) {
        List<ProjectDetailResponseDto> results = projectSearchService.searchByTitle(keyword);
        return ResponseEntity.ok(projectSearchService.searchByTitle(keyword));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProjectSummaryResponseDto>> searchProjects(
            @ModelAttribute ProjectSearchRequestDto searchDto,
            @PageableDefault(size = 10) Pageable pageable // 기본 페이지 크기 10
    ) {
        Page<ProjectSummaryResponseDto> results = projectSearchService.searchProjects(searchDto, pageable);
        return ResponseEntity.ok(results);
    }
} */