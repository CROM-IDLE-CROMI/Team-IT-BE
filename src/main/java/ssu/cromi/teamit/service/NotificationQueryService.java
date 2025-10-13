package ssu.cromi.teamit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.DTO.NotificationResponse;

public interface NotificationQueryService {

    /**
     * 특정 사용자의 알림을 종류별로 필터링하여 페이징 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @param type 조회할 알림의 종류 ("PROJECT", "BOARD")
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 알림 DTO 목록
     */
    Page<NotificationResponse> getNotifications(String userId, String type, Pageable pageable);
}