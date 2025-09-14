package ssu.cromi.teamit.service.postboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.postboard.BoardListResponse;
import ssu.cromi.teamit.entity.enums.BoardCategory;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.BoardRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardListServiceImpl implements BoardListService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public Page<BoardListResponse> getPosts(BoardCategory category, String keyword, Pageable pageable) {

        Page<Board> posts = boardRepository.searchBoards(category, keyword, pageable);

        List<String> authorIds = posts.getContent().stream().map(Board::getAuthorId).distinct().collect(Collectors.toList());
        Map<String, User> authorsMap = userRepository.findAllById(authorIds).stream().collect(Collectors.toMap(User::getUid, user -> user));

        return posts.map(post -> {
            User author = authorsMap.get(post.getAuthorId());
            return BoardListResponse.of(post, author);
        });
    }
}