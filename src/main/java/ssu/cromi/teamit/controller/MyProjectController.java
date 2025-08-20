package ssu.cromi.teamit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.myproject.*;
import ssu.cromi.teamit.service.MyProjectService;

import java.util.List;

@RestController
@RequestMapping("/v1/my-projects")
@RequiredArgsConstructor
public class MyProjectController {
    private final MyProjectService myProjectService;

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

    @PostMapping("/{projectId}/milestones")
    public ResponseEntity<MilestoneResponse> createProjectMilestones(@PathVariable Long projectId, @RequestBody MilestoneRequest milestoneRequest){
        MilestoneResponse newMilestone = myProjectService.createMilestone(projectId, milestoneRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMilestone);
    }
}
