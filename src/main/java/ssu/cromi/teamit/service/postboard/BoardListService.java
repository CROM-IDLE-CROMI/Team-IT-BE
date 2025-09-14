package ssu.cromi.teamit.service.postboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.DTO.postboard.BoardListResponse;
import ssu.cromi.teamit.entity.enums.BoardCategory;

public interface BoardListService {
    Page<BoardListResponse> getPosts(BoardCategory category, String keyword, Pageable pageable);
}