package ssu.cromi.teamit.entity.enums;

public enum Category {
    CONTEST("공모전"),
    COMPETITION("대회"),
    SIDE_PROJECT("사이드 프로젝트"),
    TOY_PROJECT("토이 프로젝트"),
    ETC("기타");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

