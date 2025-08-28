package ssu.cromi.teamit.DTO.myproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.entity.teamup.ProjectMember;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ProjectMemberResponse {
    private String nickName;
    private String position;
    private String profile;
    private String role;
    private List<String> techStacks;

    public static ProjectMemberResponse from(ProjectMember member){
        return ProjectMemberResponse.builder()
                .nickName(member.getUser().getNickName())
                .position(member.getPosition().getDisplayName())
                .profile(member.getUser().getProfileImg())
                .role(member.getRole().getDisplayName())
                .techStacks(member.getProjectStacks().stream()
                        .map(Stack::getTag)
                        .collect(Collectors.toList()))
                .build();
    }
}
