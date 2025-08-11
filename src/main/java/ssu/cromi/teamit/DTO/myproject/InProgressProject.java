package ssu.cromi.teamit.DTO.myproject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.enums.Platform;
import ssu.cromi.teamit.entity.enums.Position;
import ssu.cromi.teamit.entity.enums.ProjectStatus;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class InProgressProject {
    private final Long id;
    private final String projectName;
    private final String ownerId;
    private final Platform platform;
    private final Position position;
    private final LocalDateTime projectStartDate;
    private final ProjectStatus progressStatus;
}