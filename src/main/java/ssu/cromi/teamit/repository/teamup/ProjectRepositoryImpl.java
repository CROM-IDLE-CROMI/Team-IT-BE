/* package ssu.cromi.teamit.repository.teamup;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.entity.teamup.Project;

import java.util.List;

import static ssu.cromi.teamit.entity.teamup.QProject.project;

@RequiredArgsConstructor // JPAQueryFactory를 주입받기 위해 필요
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Project> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable) {
        // 1. 데이터 조회 쿼리
        List<Project> content = queryFactory
                .selectFrom(project)
                .where(
                        keywordContains(searchDto.getKeyword()),
                        categoryEq(searchDto.getCategory()),
                        platformEq(searchDto.getPlatform()),
                        meetingApproachEq(searchDto.getMeetingApproach()),
                        positionEq(searchDto.getPosition()),
                        projectStatusEq(searchDto.getProjectStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(project.createdAt.desc()) // 기본 정렬: 최신순
                .fetch();

        // 2. 전체 카운트 조회 쿼리 (페이징을 위해 필요)
        long total = queryFactory
                .selectFrom(project)
                .where(
                        keywordContains(searchDto.getKeyword()),
                        categoryEq(searchDto.getCategory()),
                        platformEq(searchDto.getPlatform()),
                        meetingApproachEq(searchDto.getMeetingApproach()),
                        positionEq(searchDto.getPosition()),
                        projectStatusEq(searchDto.getProjectStatus())
                )
                .fetchCount();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    // 아래는 각 조건이 null이 아닐 때만 where 절에 추가되도록 하는 메서드들입니다.
    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? project.title.containsIgnoreCase(keyword).or(project.content.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? project.category.eq(category) : null;
    }

    private BooleanExpression platformEq(Platform platform) {
        return platform != null ? project.platform.eq(platform) : null;
    }

    private BooleanExpression meetingApproachEq(MeetingApproach meetingApproach) {
        return meetingApproach != null ? project.meetingApproach.eq(meetingApproach) : null;
    }

    private BooleanExpression positionEq(Position position) {
        // Position은 List 형태일 수 있으므로, 해당 로직에 맞게 수정 필요 (예: any(), in())
        // 여기서는 일단 단일 값이라고 가정합니다.
        return position != null ? project.positions.any().position.eq(position) : null;
    }

    private BooleanExpression projectStatusEq(ProjectStatus projectStatus) {
        return projectStatus != null ? project.status.eq(projectStatus) : null;
    }
} */