package ssu.cromi.teamit.repository.findproject;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;

import java.util.List;
import java.util.Optional;

public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    List<ProjectApplication> findByProjectId(Long projectId);
    List<ProjectApplication> findByProjectIdAndApplicantId(Long projectId, String applicantId);
    Optional<ProjectApplication> findByProjectIdAndApplicantIdAndId(Long projectId, String applicantId, Long applicationId);
}