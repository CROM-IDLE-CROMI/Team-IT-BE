package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.myproject.*;
import ssu.cromi.teamit.security.JwtUtils;
import ssu.cromi.teamit.service.MyProjectService;
import ssu.cromi.teamit.service.ProjectReviewService;

import java.util.List;

@RestController
@RequestMapping("/v1/my-projects")
@RequiredArgsConstructor
public class MyProjectController {
    private final MyProjectService myProjectService;
    private final ProjectReviewService projectReviewService;
    private final JwtUtils jwtUtils;

    @GetMapping("/{uid}")
    public ResponseEntity<?> getMyprojects(
            @PathVariable String uid,
            @RequestParam(required = false) String status){
        if("IN_PROGRESS".equalsIgnoreCase(status)){
            List<InProgressProject> inProgressProjects = myProjectService.getInProgressProjects(uid);
            return ResponseEntity.ok(inProgressProjects);
        }
        else if ("COMPLETED".equalsIgnoreCase(status)) {
            List<CompletedProject> completedProjects = myProjectService.getCompletedProjects(uid);
            return ResponseEntity.ok(completedProjects);
        }
        else{
            MyProjectResponse allMyProjects = myProjectService.getAllMyProjects(uid);
            return ResponseEntity.ok(allMyProjects);
        }
    }
    @GetMapping("/{projectId}/milestones")
    public ResponseEntity<List<MilestoneResponse>> getProjectMilestones(@PathVariable Long projectId){
        List<MilestoneResponse> milestones = myProjectService.getMilestone(projectId);
        return ResponseEntity.ok(milestones);
    }

    @GetMapping("/{projectId}/detail")
    public ResponseEntity<MyProjectDetailResponse> getMyProjectDetail(@PathVariable Long projectId, @RequestParam(defaultValue = "5") int milestoneLimit){
        MyProjectDetailResponse projectDetail = myProjectService.getMyProjectDetail(projectId, milestoneLimit);
        return ResponseEntity.ok(projectDetail);
    }

    @PatchMapping("/{projectId}/details")
    public ResponseEntity<Void> updateProjectDetails(
            @PathVariable Long projectId,
            @Valid @RequestBody MyProjectUpdateRequest updateRequestDto) {
        myProjectService.updateProjectDetails(projectId, updateRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/milestones")
    public ResponseEntity<MilestoneResponse> createProjectMilestones(@PathVariable Long projectId, @RequestBody MilestoneRequest milestoneRequest){
        MilestoneResponse newMilestone = myProjectService.createMilestone(projectId, milestoneRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMilestone);
    }

    @PatchMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<MilestoneResponse> updateProjectMilestones(@PathVariable Long projectId, @PathVariable Long milestoneId, @RequestBody MilestoneRequest milestoneRequest){
        MilestoneResponse updateMilestone = myProjectService.updateMilestone(projectId, milestoneId, milestoneRequest);
        return ResponseEntity.ok(updateMilestone);
    }

    @PatchMapping("/{projectId}/description")
    public ResponseEntity<Void> updateProjectDescription(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectDescriptionUpdateRequest descriptionRequest) {
        myProjectService.updateProjectDescription(projectId, descriptionRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberDetailResponse>> getProjectMembers(@PathVariable Long projectId){
        List<ProjectMemberDetailResponse> members = myProjectService.getProjectMembers(projectId);
        return  ResponseEntity.ok(members);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectMemberResponse> addProjectMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddProjectMemberRequest requestDto) {
        ProjectMemberResponse newMember = myProjectService.addProjectMember(projectId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }

    @PatchMapping("/{projectId}/members/{userId}")
    public ResponseEntity<ProjectMemberResponse> updateProjectMember(
            @PathVariable Long projectId,
            @PathVariable String userId,
            @Valid @RequestBody UpdateProjectMemberRequest requestDto) {
        ProjectMemberResponse updatedMember = myProjectService.updateProjectMember(projectId, userId, requestDto);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long projectId, @PathVariable String userId){
        myProjectService.deleteProjectMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{projectId}/delegate-leadership")
    public ResponseEntity<Void> delegateLeadership(
            @PathVariable Long projectId,
            @Valid @RequestBody DelegateLeadershipRequest requestDto) {
        myProjectService.delegateLeadership(projectId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{projectId}/status")
    public ResponseEntity<Void> updateProjectStatus(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectStatusUpdateRequest requestDto,
            @RequestHeader("Authorization") String token) {
        String uid = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        myProjectService.updateProjectStatus(projectId, uid, requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/reviews")
    public ResponseEntity<ProjectReviewResponse> createReview(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectReviewRequest reviewRequest,
            @RequestHeader("Authorization") String token) {
        String reviewerId = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        reviewRequest.setProjectId(projectId);
        ProjectReviewResponse review = projectReviewService.createReview(reviewerId, reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/{projectId}/reviews")
    public ResponseEntity<List<ProjectReviewResponse>> getProjectReviews(@PathVariable Long projectId) {
        List<ProjectReviewResponse> reviews = projectReviewService.getProjectReviews(projectId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ProjectReviewResponse>> getUserReviews(@PathVariable String userId) {
        List<ProjectReviewResponse> reviews = projectReviewService.getUserReviews(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/my-reviews")
    public ResponseEntity<List<ProjectReviewResponse>> getMyReviews(@RequestHeader("Authorization") String token) {
        String reviewerId = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        List<ProjectReviewResponse> reviews = projectReviewService.getReviewsByReviewer(reviewerId);
        return ResponseEntity.ok(reviews);
    }
}
