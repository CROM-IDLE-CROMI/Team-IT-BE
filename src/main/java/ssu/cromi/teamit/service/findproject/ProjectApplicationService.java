package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.findproject.ProjectApplicationRequestDto;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;

public interface ProjectApplicationService {

    /**
     * 프로젝트 지원
     * @param dto 지원 요청 DTO
     * @param applicantId 로그인 사용자 ID
     * @param projectId 지원할 프로젝트 ID
     */
    ProjectApplication applyToProject(ProjectApplicationRequestDto dto, String applicantId, Long projectId);
}
