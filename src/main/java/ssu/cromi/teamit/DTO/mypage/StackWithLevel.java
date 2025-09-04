package ssu.cromi.teamit.DTO.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StackWithLevel {
    private String stackName;
    private String icon;
    private String level;
}