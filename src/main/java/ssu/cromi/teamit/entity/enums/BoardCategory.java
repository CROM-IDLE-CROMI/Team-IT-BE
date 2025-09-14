package ssu.cromi.teamit.entity.enums;

public enum BoardCategory {
    QUESTION("질문"),
    PROMOTION("홍보"),
    INFO("정보");

    private final String displayName;

    BoardCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}