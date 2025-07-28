package ssu.cromi.teamit.entity.enums;

public enum MemberRole {
    LEADER("팀장"),
    MEMBER("팀원");

    private final String displayName;

    MemberRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}