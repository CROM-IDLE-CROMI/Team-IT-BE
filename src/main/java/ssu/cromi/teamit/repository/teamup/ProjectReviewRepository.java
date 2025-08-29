package ssu.cromi.teamit.repository.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.entity.teamup.ProjectReview;

import java.util.List;

@Repository
public interface ProjectReviewRepository extends JpaRepository<ProjectReview, Long> {
    
    List<ProjectReview> findByProjectId(Long projectId);
    
    List<ProjectReview> findByReviewerId(String reviewerId);
    
    List<ProjectReview> findByRevieweeId(String revieweeId);
    
    @Query("SELECT AVG(pr.score) FROM ProjectReview pr WHERE pr.revieweeId = :revieweeId")
    Double findAverageScoreByRevieweeId(@Param("revieweeId") String revieweeId);
    
    boolean existsByProjectIdAndReviewerIdAndRevieweeId(Long projectId, String reviewerId, String revieweeId);
}