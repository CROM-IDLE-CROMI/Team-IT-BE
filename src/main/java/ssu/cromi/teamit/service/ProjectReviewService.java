package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.myproject.ProjectReviewRequest;
import ssu.cromi.teamit.DTO.myproject.ProjectReviewResponse;

import java.util.List;

public interface ProjectReviewService {
    
    /**
     * 새로운 리뷰 작성
     * @param reviewerId 리뷰 작성자 ID
     * @param reviewRequest 리뷰 요청 정보
     * @return 생성된 리뷰 정보
     */
    ProjectReviewResponse createReview(String reviewerId, ProjectReviewRequest reviewRequest);
    
    /**
     * 특정 프로젝트의 모든 리뷰 조회
     * @param projectId 프로젝트 ID
     * @return 해당 프로젝트의 모든 리뷰 목록
     */
    List<ProjectReviewResponse> getProjectReviews(Long projectId);
    
    /**
     * 특정 사용자에 대한 모든 리뷰 조회
     * @param revieweeId 리뷰 대상 사용자 ID
     * @return 해당 사용자에 대한 모든 리뷰 목록
     */
    List<ProjectReviewResponse> getUserReviews(String revieweeId);
    
    /**
     * 특정 사용자가 작성한 모든 리뷰 조회
     * @param reviewerId 리뷰 작성자 ID
     * @return 해당 사용자가 작성한 모든 리뷰 목록
     */
    List<ProjectReviewResponse> getReviewsByReviewer(String reviewerId);
    
    /**
     * 사용자의 평균 점수 업데이트
     * @param revieweeId 리뷰 대상 사용자 ID
     */
    void updateUserAverageScore(String revieweeId);
}