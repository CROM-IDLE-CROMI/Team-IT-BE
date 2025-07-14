package ssu.cromi.teamit.entity;

public enum Platform {
    WEB("웹"),
    APP("앱"),
    GAME("게임"),
    ETC("기타");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
