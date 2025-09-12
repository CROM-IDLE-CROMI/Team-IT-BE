package ssu.cromi.teamit.DTO.findproject;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ssu.cromi.teamit.entity.enums.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectSearchRequestDto {

    // 텍스트 검색 (제목 또는 내용)
    private String keyword;

    // Enum 필터 조건들
    private Platform platform;
    private MeetingApproach meetingApproach;
    private ProjectStatus projectStatus;


    // JSON 배열 필터 조건들 (쉼표로 구분된 문자열로 요청, 예: "java,spring,react")
    private List<String> recruitPositions;
    private List<String> requireStack;
    private List<String> locations;

    // 기간 필터 조건들
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recruitingDeadline; // 모집 마감일 (이 날짜까지 모집하는 글)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate projectStartDate;   // 프로젝트 시작일 (이 날짜 이후에 시작)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate projectEndDate;     // 프로젝트 종료일 (이 날짜 이전에 종료)
}