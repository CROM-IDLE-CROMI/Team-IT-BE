package ssu.cromi.teamit.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.myproject.ProjectReviewRequest;
import ssu.cromi.teamit.DTO.myproject.ProjectReviewResponse;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.teamup.ProjectReview;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.teamup.ProjectReviewRepository;
import ssu.cromi.teamit.service.ProjectReviewService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectReviewServiceImpl implements ProjectReviewService {

    private final ProjectReviewRepository projectReviewRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectReviewResponse createReview(String reviewerId, ProjectReviewRequest reviewRequest) {
        if (projectReviewRepository.existsByProjectIdAndReviewerIdAndRevieweeId(
                reviewRequest.getProjectId(), reviewerId, reviewRequest.getRevieweeId())) {
            throw new IllegalStateException("이미 해당 멤버에 대한 리뷰를 작성하셨습니다.");
        }

        if (reviewerId.equals(reviewRequest.getRevieweeId())) {
            throw new IllegalArgumentException("자기 자신에게는 리뷰를 작성할 수 없습니다.");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 작성자를 찾을 수 없습니다."));
        User reviewee = userRepository.findById(reviewRequest.getRevieweeId())
                .orElseThrow(() -> new IllegalArgumentException("리뷰 대상자를 찾을 수 없습니다."));
        Project project = projectRepository.findById(reviewRequest.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        ProjectReview review = ProjectReview.builder()
                .projectId(reviewRequest.getProjectId())
                .reviewerId(reviewerId)
                .revieweeId(reviewRequest.getRevieweeId())
                .score(reviewRequest.getScore())
                .content(reviewRequest.getContent())
                .project(project)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .build();

        ProjectReview savedReview = projectReviewRepository.save(review);
        
        updateUserAverageScore(reviewRequest.getRevieweeId());

        return convertToResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectReviewResponse> getProjectReviews(Long projectId) {
        List<ProjectReview> reviews = projectReviewRepository.findByProjectId(projectId);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectReviewResponse> getUserReviews(String revieweeId) {
        List<ProjectReview> reviews = projectReviewRepository.findByRevieweeId(revieweeId);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectReviewResponse> getReviewsByReviewer(String reviewerId) {
        List<ProjectReview> reviews = projectReviewRepository.findByReviewerId(reviewerId);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserAverageScore(String revieweeId) {
        Double averageScore = projectReviewRepository.findAverageScoreByRevieweeId(revieweeId);
        
        User user = userRepository.findById(revieweeId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        user.setMyScore(averageScore);
        userRepository.save(user);
        
        log.info("사용자 {}의 평균 점수가 {}로 업데이트되었습니다.", revieweeId, averageScore);
    }

    private ProjectReviewResponse convertToResponse(ProjectReview review) {
        return ProjectReviewResponse.builder()
                .id(review.getId())
                .projectId(review.getProjectId())
                .reviewerId(review.getReviewerId())
                .reviewerNickName(review.getReviewer().getNickName())
                .revieweeId(review.getRevieweeId())
                .revieweeNickName(review.getReviewee().getNickName())
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}