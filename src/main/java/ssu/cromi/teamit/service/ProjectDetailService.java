package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.ProjectDetailResponseDto;

public interface ProjectDetailService {

    ProjectDetailResponseDto getProjectDetail(Long projectId);
}
