package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.postboard.BoardListResponse;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import java.util.List;

public interface MainPageService {
    List<ProjectSummaryResponseDto> getPopularProjects();
    List<BoardListResponse> getHotBoards();
}