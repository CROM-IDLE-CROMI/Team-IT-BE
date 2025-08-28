package ssu.cromi.teamit.repository.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.ProjectMember;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByUserId(String userId);
    List<ProjectMember> findByUser(User user);
    List<ProjectMember> findByProjectId(Long projectId);
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, String userId);
}
