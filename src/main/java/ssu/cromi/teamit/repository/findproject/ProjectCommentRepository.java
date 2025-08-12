package ssu.cromi.teamit.repository.findproject;


import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.findproject.ProjectComment;

import java.util.List;

public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findAllByProjectId(Long projectId);
}
