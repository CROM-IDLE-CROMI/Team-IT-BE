package ssu.cromi.teamit.repository.findproject;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;

public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
}
