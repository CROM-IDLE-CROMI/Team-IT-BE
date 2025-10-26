package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

@Getter
public class MemberAddedEvent {
    private final Project project;
    private final User addedUser;

    public MemberAddedEvent(Project project, User addedUser) {
        this.project = project;
        this.addedUser = addedUser;
    }
}