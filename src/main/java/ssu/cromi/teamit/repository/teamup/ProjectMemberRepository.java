package ssu.cromi.teamit.repository.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectId(Long projectId);
}
