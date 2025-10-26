package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.entity.postboard.ScrapId;
import ssu.cromi.teamit.event.BoardScrappedEvent;
import ssu.cromi.teamit.exception.BoardNotFoundException;
import ssu.cromi.teamit.exception.ScrapAlreadyExistsException;
import ssu.cromi.teamit.exception.ScrapNotFoundException;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.BoardRepository;
import ssu.cromi.teamit.repository.postboard.ScrapRepository;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final ApplicationEventPublisher eventPublisher;

    private final ScrapRepository scrapRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Scrap createScrap(Long postId, String userId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글입니다. id=" + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다. id=" + userId));

        ScrapId scrapId = new ScrapId(userId, postId);
        if (scrapRepository.existsById(scrapId)) {
            throw new ScrapAlreadyExistsException("이미 스크랩한 게시글입니다. id=" + scrapId);
        }

        Scrap scrap = Scrap.builder().user(user).board(board).build();
        Scrap savedScrap = scrapRepository.save(scrap);

        // 게시글의 스크랩 수 1 증가
        board.setScrapCount(board.getScrapCount() + 1);

        eventPublisher.publishEvent(new BoardScrappedEvent(savedScrap));
        return savedScrap;
    }

    @Override
    @Transactional
    public void deleteScrap(Long postId, String userId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글입니다. id=" + postId));

        ScrapId scrapId = new ScrapId(userId, postId);
        if (!scrapRepository.existsById(scrapId)) {
            throw new ScrapNotFoundException("스크랩하지 않은 게시글입니다. id=" + scrapId);
        }

        scrapRepository.deleteById(scrapId);

        // 게시글의 스크랩 수 1 감소
        board.setScrapCount(board.getScrapCount() - 1);
    }
}