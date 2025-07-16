package ssu.cromi.teamit.entity.enums;

public enum Position {
    UNDECIDED("미정"),
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    DESIGNER("디자이너"),
    PLANNER("기획자"),
    PM("PM");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
