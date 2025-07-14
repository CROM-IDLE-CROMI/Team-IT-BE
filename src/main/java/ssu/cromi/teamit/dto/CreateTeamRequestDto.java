package ssu.cromi.teamit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor

public class CreateTeamRequestDto {
    private String createrId;
    private String memberPosition;
    private int memberNum;
    private List<String> requireStack;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String platform;
    private List<String> position;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String projectStatus;
    private String title;
    private String ideaExplain;
    private String projectName;
    private String meetingApproach;
    private String location;
    private String minRequest;
    private String questions;
}
