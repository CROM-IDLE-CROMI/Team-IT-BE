package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

@Getter
public class MemberRemovedEvent {
    private final Project project; // 멤버가 삭제된 프로젝트
    private final User removedUser;  // 삭제된 사용자

    public MemberRemovedEvent(Project project, User removedUser) {
        this.project = project;
        this.removedUser = removedUser;
    }
}