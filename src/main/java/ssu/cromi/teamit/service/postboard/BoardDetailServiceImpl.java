package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.postboard.BoardDetailResponse;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.exception.BoardNotFoundException;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.postboard.BoardRepository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BoardDetailServiceImpl implements BoardDetailService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BoardDetailResponse getBoardDetail(Long postId) {
        int updated = boardRepository.incrementViewCount(postId);
        if (updated == 0) {
            throw new BoardNotFoundException("존재하지 않는 게시글입니다. id=" + postId);
        }

        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글입니다. id=" + postId));

        User author = userRepository.findById(board.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException("작성자 정보를 찾을 수 없습니다. id=" + board.getAuthorId()));

        return BoardDetailResponse.of(board, author);
    }
}