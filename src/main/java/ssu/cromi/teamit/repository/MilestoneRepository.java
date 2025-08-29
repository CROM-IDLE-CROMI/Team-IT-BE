package ssu.cromi.teamit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.Milestone;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByProjectId(Long projectId);

    /**
     * 특정 프로젝트에서 완료되지 않은 마일스톤을 조건에 맞게 정렬하여 상위 N개 조회
     * @param projectId 프로젝트 ID
     * @param progress 비교할 진행률 (예: 100 미만)
     * @param pageable 정렬 조건 및 개수 제한 (PageRequest.of(0, N, Sort.by(...)))
     * @return List<Milestone>
     */
    List<Milestone> findByProjectIdAndProgressLessThan(Long projectId, int progress, Pageable pageable);

    /**
     * 특정 프로젝트에서 특정 담당자에게 할당된 모든 마일스톤 조회
     * @param projectId 프로젝트 ID
     * @param assignee 담당자 User 객체
     * @return List<Milestone>
     */
    List<Milestone> findByProjectIdAndAssignee(Long projectId, User assignee);

}
