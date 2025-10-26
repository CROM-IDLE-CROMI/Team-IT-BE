// DB 접근

package ssu.cromi.teamit.repository.teamup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.entity.teamup.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Project p set p.viewCount = p.viewCount + 1 where p.id = :id")
    int incrementViewCount(@Param("id") Long id);

    // 조회수 높은 순으로 상위 4개를 조회
    List<Project> findTop4ByStatusInOrderByViewCountDesc(List<Status> statuses);

    // 프로젝트 찾기 메인페이지를 위한
    Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);

}