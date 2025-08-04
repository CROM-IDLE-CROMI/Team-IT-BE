package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;

public interface ProjectDetailService {

    ProjectDetailResponseDto getProjectDetail(Long projectId);
}
