// 검색 조건에 대한 DTO
package ssu.cromi.teamit.DTO.findproject;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ssu.cromi.teamit.entity.enums.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSearchRequestDto {

    // 텍스트 검색 (제목 또는 내용)
    private String keyword;

    // Enum 필터 조건들
    private Category category;
    private Platform platform;
    private MeetingApproach meetingApproach;
    private Position position;
    private ProjectStatus projectStatus;

    // 나중에 정렬 기능 추가를 위한 필드 (예: "latest", "views")
    private String sortBy;

    // 더 추가해야 하는 것 기술 스택 스트링으로 받아서
    // 프로젝트 모집 종료 기간 기준으로, 위치도 스트링으로?, 프로젝트 기간도추가필요

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadlineBefore; // 프로젝트 모집 마감일 이전
}
