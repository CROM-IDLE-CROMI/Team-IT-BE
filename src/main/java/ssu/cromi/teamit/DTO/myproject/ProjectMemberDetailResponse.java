package ssu.cromi.teamit.DTO.myproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.entity.teamup.ProjectMember;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProjectMemberDetailResponse {
    private String nickname;
    private String email;
    private String position;
    private List<String> techStacks;
    private Double developerRating;

    public static ProjectMemberDetailResponse from(ProjectMember member){
        return ProjectMemberDetailResponse.builder()
                .nickname(member.getUser().getNickName())
                .email(member.getUser().getEmail())
                .position(member.getPosition().getDisplayName())
                .techStacks(member.getUser().getStacks().stream()
                        .map(Stack::getTag)
                        .collect(Collectors.toList()))
                .developerRating(member.getUser().getMyScore())
                .build();
    }
}
