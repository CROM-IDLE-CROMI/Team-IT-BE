// DB 접근

package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.Project;

public interface TeamRepository extends JpaRepository<Project, Long> {
}