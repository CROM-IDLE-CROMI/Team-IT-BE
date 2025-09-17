package ssu.cromi.teamit.controller.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.ScrapService;

@RestController
@RequestMapping("/api/board/{postId}/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<Void> createScrap(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        scrapService.createScrap(postId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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