package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.findproject.ProjectApplyPageResponseDto;

public interface ProjectApplyPageService {
    ProjectApplyPageResponseDto getApplicationPage(Long projectId);
}
