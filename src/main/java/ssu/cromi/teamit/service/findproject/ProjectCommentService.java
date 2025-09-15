package ssu.cromi.teamit.service.findproject;

import ssu.cromi.teamit.DTO.findproject.CommentUpdateRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectCommentResponseDto;
import ssu.cromi.teamit.entity.findproject.ProjectComment;

import java.util.List;

public interface ProjectCommentService {
    ProjectComment createComment(Long projectId, String userId, ProjectCommentRequestDto dto);
    List<ProjectCommentResponseDto> getCommentsByProjectId(Long projectId);

    ProjectComment updateComment(Long commentId, String currentUserId, CommentUpdateRequestDto dto);
    void deleteComment(Long commentId, String currentUserId);
}