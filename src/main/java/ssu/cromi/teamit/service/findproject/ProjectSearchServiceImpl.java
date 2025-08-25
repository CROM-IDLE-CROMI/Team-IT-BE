package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.DTO.findproject.ProjectSummaryResponseDto;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 기능이므로 readOnly = true 설정
public class ProjectSearchServiceImpl implements ProjectSearchService {

    private final ProjectRepository projectRepository;

    /**
     * Controller로부터 검색 조건(DTO)과 페이징 정보를 받아
     * Repository에 전달하고, 그 결과를 DTO로 변환하여 반환
     */
    @Override
    public Page<ProjectSummaryResponseDto> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable) {
        // Repository를 호출하여 조건에 맞는 Project 엔티티 목록을 Page 형태로 받는다
        // 이 때 ProjectRepositoryImpl의 QueryDSL 코드가 실행
        return projectRepository.searchProjects(searchDto, pageable)
                // Stream의 map 기능을 이용해 Page<Project>를 Page<ProjectSummaryResponseDto>로 변환
                // ProjectSummaryResponseDto::from 메서드가 각 Project 엔티티를 DTO로 변환
                .map(ProjectSummaryResponseDto::from);
    }
}