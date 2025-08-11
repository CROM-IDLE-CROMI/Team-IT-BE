package ssu.cromi.teamit.dto.myproject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.enums.Category;
import ssu.cromi.teamit.entity.enums.Position;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CompletedProject {
    private final Long id;
    private final String projectName;
    private final Position position;
    private final List<String> requireStack;
    private final Category type;
    private final LocalDateTime projectStartDate;
    private final boolean isCompleted;
}
