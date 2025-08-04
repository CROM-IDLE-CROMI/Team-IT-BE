package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.ProjectDetailResponseDto;

public interface ProjectDetailService {

    ProjectDetailResponseDto getProjectDetail(Long projectId);
}
