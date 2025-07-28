package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}
