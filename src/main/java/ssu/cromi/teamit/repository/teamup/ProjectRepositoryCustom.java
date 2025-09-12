package ssu.cromi.teamit.repository.teamup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.entity.teamup.Project;

public interface ProjectRepositoryCustom {

    // QueryDSL로 구현할 동적 검색 메서드를 여기에 선언
    Page<Project> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable);

}