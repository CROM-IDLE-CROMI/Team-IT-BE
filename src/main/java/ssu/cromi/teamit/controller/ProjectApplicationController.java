package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ssu.cromi.teamit.dto.ProjectApplicationRequestDto;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.ProjectApplicationService;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectApplicationController {

    private final ProjectApplicationService projectApplicationService;

    /**
     * 프로젝트 지원
     * @param projectId 지원할 프로젝트 ID
     * @param dto 지원서 요청 본문
     * @param userDetails 로그인 사용자
     * @return 생성 완료 응답
     */
    @PostMapping("/{projectid}/submission")
    public ResponseEntity<Void> applyToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectApplicationRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String applicantId = userDetails.getUid(); // 로그인된 사용자 ID
        projectApplicationService.applyToProject(dto, applicantId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
