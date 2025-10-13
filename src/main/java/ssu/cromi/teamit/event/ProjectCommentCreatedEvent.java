package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.findproject.ProjectComment;

@Getter
public class ProjectCommentCreatedEvent {
    private final ProjectComment comment;

    public ProjectCommentCreatedEvent(ProjectComment comment) {
        this.comment = comment;
    }
}