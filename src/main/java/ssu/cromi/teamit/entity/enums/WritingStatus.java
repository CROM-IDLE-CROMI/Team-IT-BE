package ssu.cromi.teamit.entity.enums;

public enum WritingStatus {
    IN_PROGRESS("작성중"),
    DRAFT("임시저장"),
    COMPLETED("작성완료");

    private final String displayName;

    WritingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
