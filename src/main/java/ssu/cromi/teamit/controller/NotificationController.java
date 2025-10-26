package ssu.cromi.teamit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.NotificationResponse;
import ssu.cromi.teamit.DTO.common.PageResponse;
import ssu.cromi.teamit.response.ApiResponse;
import ssu.cromi.teamit.security.UserDetailsImpl;
import ssu.cromi.teamit.service.NotificationQueryService;
import ssu.cromi.teamit.service.NotificationService;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(name = "type", defaultValue = "PROJECT") String type,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        String userId = userDetails.getUid();
        Page<NotificationResponse> notifications = notificationQueryService.getNotifications(userId, type, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "알림 목록이 성공적으로 조회되었습니다.", PageResponse.of(notifications))
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