package ssu.cromi.teamit.DTO.postboard;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * 페이징 조회 결과를 클라이언트에 전달하기 위한 DTO
 * @param <T> 페이징될 데이터의 DTO 타입
 */
@Getter
public class PageResponse<T> {

    private final List<T> content;        // 현재 페이지의 데이터 목록
    private final int pageNumber;       // 현재 페이지 번호 (0부터 시작)
    private final int pageSize;         // 한 페이지에 표시되는 데이터 수
    private final int totalPages;       // 전체 페이지 수
    private final long totalElements;   // 전체 데이터 수
    private final boolean isLast;         // 마지막 페이지 여부

    // Spring의 Page 객체를 받아 필요한 페이징 정보만 추출하여 생성
    private PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.isLast = page.isLast();
    }

    /**
     * Page 객체를 PageResponse DTO로 변환하는 정적 팩토리 메서드
     * @param page 변환할 Page 객체
     * @return 생성된 PageResponse DTO
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page);
    }
}