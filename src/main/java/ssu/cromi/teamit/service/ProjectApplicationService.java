package ssu.cromi.teamit.service;

import ssu.cromi.teamit.dto.ProjectApplicationRequestDto;

public interface ProjectApplicationService {

    /**
     * 프로젝트 지원
     * @param dto 지원 요청 DTO
     * @param applicantId 로그인 사용자 ID
     * @param projectId 지원할 프로젝트 ID
     */
    void applyToProject(ProjectApplicationRequestDto dto, String applicantId, Long projectId);
}
