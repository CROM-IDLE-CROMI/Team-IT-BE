package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.ProjectApplication;

public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
}
