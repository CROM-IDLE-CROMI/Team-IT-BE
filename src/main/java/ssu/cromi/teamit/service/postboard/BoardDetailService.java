package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.BoardDetailResponse;

public interface BoardDetailService {

    BoardDetailResponse getBoardDetail(Long postId);
}