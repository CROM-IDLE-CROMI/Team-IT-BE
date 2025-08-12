// DB 접근

package ssu.cromi.teamit.repository.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.entity.teamup.Project;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Project p set p.viewCount = p.viewCount + 1 where p.id = :id")
    int incrementViewCount(@Param("id") Long id);

    List<Project> findByTitleContainingIgnoreCase(String keyword);
}