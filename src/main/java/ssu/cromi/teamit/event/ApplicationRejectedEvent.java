package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;

@Getter
public class ApplicationRejectedEvent {
    private final ProjectApplication application;

    public ApplicationRejectedEvent(ProjectApplication application) {
        this.application = application;
    }
}