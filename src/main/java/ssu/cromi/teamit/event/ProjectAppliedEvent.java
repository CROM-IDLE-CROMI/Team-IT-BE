package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.findproject.ProjectApplication;

@Getter
public class ProjectAppliedEvent {
    private final ProjectApplication application;

    public ProjectAppliedEvent(ProjectApplication application) {
        this.application = application;
    }
}