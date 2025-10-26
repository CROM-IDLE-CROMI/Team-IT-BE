package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.teamup.Project;

@Getter
public class LeadershipDelegatedEvent {
    private final Project project;
    private final User newLeader;

    public LeadershipDelegatedEvent(Project project, User newLeader) {
        this.project = project;
        this.newLeader = newLeader;
    }
}