package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.DTO.postboard.BoardListResponse;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.entity.enums.Status;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.postboard.BoardRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainPageServiceImpl implements MainPageService {

    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProjectSummaryResponseDto> getPopularProjects() {
        // 모집중이거나 추가모집중인 프로젝트만 조회
        List<Status> statusesToShow = List.of(Status.RECRUITING, Status.ADDITIONAL);
        List<Project> projects = projectRepository.findTop4ByStatusInOrderByViewCountDesc(statusesToShow);

        // 작성자 정보를 조회
        if (projects.isEmpty()) {
            return List.of();
        }

        Map<String, User> userMap = userRepository.findAllById(
                projects.stream()
                        .map(Project::getCreatorId)
                        .collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getUid, u -> u));

        return projects.stream()
                .map(project -> {
                    User creator = userMap.get(project.getCreatorId());
                    return ProjectSummaryResponseDto.from(project, creator);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardListResponse> getHotBoards() {
        List<Board> boards = boardRepository.findTop4ByOrderByScrapCountDesc();

        if (boards.isEmpty()) {
            return List.of();
        }
        List<String> authorIds = boards.stream().map(Board::getAuthorId).distinct().collect(Collectors.toList());
        Map<String, User> authorsMap = userRepository.findAllById(authorIds).stream().collect(Collectors.toMap(User::getUid, user -> user));

        return boards.stream()
                .map(board -> BoardListResponse.of(board, authorsMap.get(board.getAuthorId())))
                .collect(Collectors.toList());
    }
}