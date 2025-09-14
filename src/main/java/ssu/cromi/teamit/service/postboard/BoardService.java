package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.BoardCreateRequest;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.DTO.postboard.BoardUpdateRequest;

public interface BoardService {

    // 게시글 생성 메서드
    Board createBoard(BoardCreateRequest requestDto, String authorId);

    // 게시글 수정
    Board updateBoard(Long postId, BoardUpdateRequest requestDto, String currentUserId);

    // 게시글 삭제
    void deleteBoard(Long postId, String currentUserId);
}
