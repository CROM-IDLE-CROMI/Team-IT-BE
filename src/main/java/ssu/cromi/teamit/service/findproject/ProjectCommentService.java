package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.findproject.ProjectCommentRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentResponseDto;

import java.util.List;

public interface ProjectCommentService {
    void createComment(Long projectId, String userId, ProjectCommentRequestDto dto);
    List<ProjectCommentResponseDto> getCommentsByProjectId(Long projectId);
}