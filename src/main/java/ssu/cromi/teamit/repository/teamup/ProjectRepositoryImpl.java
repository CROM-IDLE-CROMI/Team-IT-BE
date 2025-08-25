package ssu.cromi.teamit.repository.teamup;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression; // StringExpression 임포트
import com.querydsl.core.types.dsl.StringTemplate; // StringTemplate 임포트
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ssu.cromi.teamit.DTO.findproject.ProjectSearchRequestDto;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.entity.teamup.Project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ssu.cromi.teamit.entity.teamup.QProject.project;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Project> searchProjects(ProjectSearchRequestDto searchDto, Pageable pageable) {

        JPAQuery<Project> baseQuery = queryFactory
                .selectFrom(project)
                .where(
                        // 텍스트
                        keywordContains(searchDto.getKeyword()),

                        // Enum
                        platformEq(searchDto.getPlatform()),
                        meetingApproachEq(searchDto.getMeetingApproach()),
                        projectStatusEq(searchDto.getProjectStatus()),

                        // JSON List<String>
                        recruitPositionsContain(searchDto.getRecruitPositions()),
                        requireStackContain(searchDto.getRequireStack()),
                        locationsContain(searchDto.getLocations()),

                        // Date
                        recruitingDeadlineGoe(searchDto.getRecruitingDeadline()),
                        projectPeriodBetween(searchDto.getProjectStartDate(), searchDto.getProjectEndDate())
                );

        List<Project> content = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(project.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(project.count())
                .from(project)
                .where(baseQuery.getMetadata().getWhere());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // private BooleanExpression 메서드
    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? project.title.containsIgnoreCase(keyword).or(project.ideaExplain.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression platformEq(Platform platform) {
        return platform != null ? project.platform.eq(platform) : null;
    }

    private BooleanExpression meetingApproachEq(MeetingApproach meetingApproach) {
        return meetingApproach != null ? project.meetingApproach.eq(meetingApproach) : null;
    }

    private BooleanExpression projectStatusEq(ProjectStatus projectStatus) {
        return projectStatus != null ? project.projectStatus.eq(projectStatus) : null;
    }

    // List<String> (JSON) 조건 메서드
    private BooleanExpression recruitPositionsContain(List<String> positions) {
        // DB의 JSON 배열을 문자열로 캐스팅하여 containsList 헬퍼 메서드에 전달
        StringTemplate field = Expressions.stringTemplate("CAST({0} as char)", project.recruitPositions);
        return containsList(field, positions);
    }

    private BooleanExpression requireStackContain(List<String> stacks) {
        StringTemplate field = Expressions.stringTemplate("CAST({0} as char)", project.requireStack);
        return containsList(field, stacks);
    }

    private BooleanExpression locationsContain(List<String> locations) {
        StringTemplate field = Expressions.stringTemplate("CAST({0} as char)", project.locations);
        return containsList(field, locations);
    }

    // 파라미터 타입을 StringPath에서 StringExpression으로 변경
    private BooleanExpression containsList(StringExpression field, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        BooleanExpression result = null;
        for (String value : values) {
            // JSON 배열 안에 "value" 형태로 저장되어 있으므로, 검색 시 쌍따옴표를 포함
            BooleanExpression expression = field.containsIgnoreCase("\"" + value.trim() + "\"");
            if (result == null) {
                result = expression;
            } else {
                result = result.or(expression);
            }
        }
        return result;
    }

    // 날짜 조건
    private BooleanExpression recruitingDeadlineGoe(LocalDate deadline) {
        return deadline != null ? project.validTo.goe(deadline.atTime(LocalTime.MAX)) : null;
    }

    private BooleanExpression projectPeriodBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        BooleanExpression startCondition = endDate != null ? project.startDate.loe(endDate.atTime(LocalTime.MAX)) : null;
        BooleanExpression endCondition = startDate != null ? project.endDate.goe(startDate.atStartOfDay()) : null;

        return Expressions.allOf(startCondition, endCondition);
    }
}