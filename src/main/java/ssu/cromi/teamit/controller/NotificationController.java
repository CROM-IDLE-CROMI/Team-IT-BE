package ssu.cromi.teamit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.NotificationResponse;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/board")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        List<NotificationResponse> notifications = notificationService.getBoardNotifications(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "게시판 관련 알림 목록이 성공적으로 조회되었습니다.", notifications)
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUid();
        notificationService.deleteNotification(notificationId, userId);

        return ResponseEntity.ok(new ApiResponse<>(200, "알림이 성공적으로 삭제되었습니다.", null));
    }
}