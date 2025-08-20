/* package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectSearchServiceImpl implements ProjectSearchService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDetailResponseDto> search(String title, List<String> keywords, Pageable pageable) {
        boolean noTitle = (title == null || title.isBlank());
        boolean noKeywords = (keywords == null || keywords.isEmpty());

        // 키워드 정리: trim + distinct + 빈값 제거
        if (!noKeywords) {
            keywords = keywords.stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(String::trim)
                    .distinct()
                    .toList();
            noKeywords = keywords.isEmpty();
        }
        return Page.empty();
    }
} */