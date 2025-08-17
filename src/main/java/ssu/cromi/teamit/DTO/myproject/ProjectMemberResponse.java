package ssu.cromi.teamit.DTO.myproject;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.teamup.ProjectMember;

@Builder
@Getter
public class ProjectMemberResponse {
    private String nickName;
    private String position;
    private String profile;

    public static ProjectMemberResponse from(ProjectMember member){
        return ProjectMemberResponse.builder()
                .nickName(member.getUser().getNickName())
                .position(member.getPosition().getDisplayName())
                .profile(member.getUser().getProfileImg())
                .build();
    }
}
