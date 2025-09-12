package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.entity.postboard.ScrapId;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.PostRepository;
import ssu.cromi.teamit.repository.postboard.PostScrapRepository;

@Service
@RequiredArgsConstructor
public class PostScrapServiceImpl implements PostScrapService {

    private final PostScrapRepository scrapRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createScrap(Long postId, String userId) {
        Board board = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        ScrapId scrapId = new ScrapId(userId, postId);
        if (scrapRepository.existsById(scrapId)) {
            throw new IllegalStateException("이미 스크랩한 게시글입니다.");
        }

        Scrap scrap = Scrap.builder().user(user).post(board).build();
        scrapRepository.save(scrap);

        // 게시글의 스크랩 수 1 증가
        board.setScrapCount(board.getScrapCount() + 1);
    }

    @Override
    @Transactional
    public void deleteScrap(Long postId, String userId) {
        Board board = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        ScrapId scrapId = new ScrapId(userId, postId);
        if (!scrapRepository.existsById(scrapId)) {
            throw new IllegalStateException("스크랩하지 않은 게시글입니다.");
        }

        scrapRepository.deleteById(scrapId);

        // 게시글의 스크랩 수 1 감소
        board.setScrapCount(board.getScrapCount() - 1);
    }
}