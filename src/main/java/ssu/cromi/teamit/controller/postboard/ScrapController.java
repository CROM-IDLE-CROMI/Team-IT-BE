package ssu.cromi.teamit.controller.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.DTO.postboard.ScrapResponse;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.ScrapService;

@RestController
@RequestMapping("/v1/board/{postId}/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<ApiResponse<ScrapResponse>> createScrap(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        Scrap createdScrap = scrapService.createScrap(postId, userId);

        ScrapResponse responseDto = ScrapResponse.from(createdScrap);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "스크랩이 등록되었습니다.", responseDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteScrap(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        scrapService.deleteScrap(postId, userId);
        return ResponseEntity.noContent().build();
    }
}