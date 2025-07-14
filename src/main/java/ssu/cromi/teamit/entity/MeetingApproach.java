package ssu.cromi.teamit.entity;

public enum MeetingApproach {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    HYBRID("온오프라인");

    private final String displayName;

    MeetingApproach(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

