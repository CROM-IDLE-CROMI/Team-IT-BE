package ssu.cromi.teamit.controller.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.DTO.postboard.BoardListResponse;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.service.findproject.MainPageService;

import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectMainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/popular-projects")
    public ResponseEntity<ApiResponse<List<ProjectSummaryResponseDto>>> getPopularProjects() {
        List<ProjectSummaryResponseDto> projects = mainPageService.getPopularProjects();
        return ResponseEntity.ok(new ApiResponse<>(200, "인기 프로젝트 조회 성공", projects));
    }

    @GetMapping("/hot-boards")
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> getHotBoards() {
        List<BoardListResponse> boards = mainPageService.getHotBoards();
        return ResponseEntity.ok(new ApiResponse<>(200, "핫한 게시물 조회 성공", boards));
    }
}