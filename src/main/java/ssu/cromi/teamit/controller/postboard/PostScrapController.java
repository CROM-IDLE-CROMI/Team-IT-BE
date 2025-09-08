package ssu.cromi.teamit.controller.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.postboard.PostScrapService;

@RestController
@RequestMapping("/api/posts/{postId}/scrap")
@RequiredArgsConstructor
public class PostScrapController {

    private final PostScrapService scrapService;

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
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}