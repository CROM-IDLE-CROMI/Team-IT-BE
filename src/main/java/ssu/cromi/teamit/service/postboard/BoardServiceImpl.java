package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.postboard.BoardCreateRequest;
import ssu.cromi.teamit.DTO.postboard.BoardUpdateRequest;
import ssu.cromi.teamit.entity.enums.WritingStatus;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.repository.postboard.BoardRepository;
import ssu.cromi.teamit.exception.BoardNotFoundException;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Board createBoard(BoardCreateRequest requestDto, String authorId) {
        Board newBoard = Board.builder()
                .authorId(authorId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .build();

        newBoard.setStatus(WritingStatus.COMPLETED);
        return boardRepository.save(newBoard);
    }

    @Override
    @Transactional
    public Board updateBoard(Long postId, BoardUpdateRequest requestDto, String currentUserId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        if (!board.getAuthorId().equals(currentUserId)) {
            throw new SecurityException("게시글을 수정할 권한이 없습니다.");
        } // 애초에 수정하기 버튼이 뜨지 않도록 프론트에서 처리

        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setCategory(requestDto.getCategory());

        return board;
    }

    @Override
    @Transactional
    public void deleteBoard(Long postId, String currentUserId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        if (!board.getAuthorId().equals(currentUserId)) {
            throw new SecurityException("게시글을 삭제할 권한이 없습니다.");
        } // 삭제하기가 애초에 안뜨도록 프론트에서 처리

        boardRepository.delete(board);
    }
}