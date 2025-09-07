package ssu.cromi.teamit.entity.enums;

public enum PostStatus {
    DRAFT("임시 저장"),
    PUBLISHED("게시됨");

    private final String displayName;

    PostStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}