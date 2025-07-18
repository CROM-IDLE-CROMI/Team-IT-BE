package ssu.cromi.teamit.entity.enums;

public enum ProjectStatus {
    IDEA("구상"),
    DESIGN("기획"),
    IN_PROGRESS("개발 진행중"),
    ETC("기타");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}