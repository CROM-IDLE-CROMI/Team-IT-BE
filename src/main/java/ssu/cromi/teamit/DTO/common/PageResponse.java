package ssu.cromi.teamit.DTO.common;

import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T>{
    private int page;
    private int size;
    private List<T> content;
    private long totalElements;
    private int totalPages;

    /**
     * Spring Data Page<T> 를 기반으로 PageResponseDto 생성
     */
    public static <T> PageResponse<T> of(Page<T> pageData) {
        return new PageResponse<>(
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getContent(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }

}