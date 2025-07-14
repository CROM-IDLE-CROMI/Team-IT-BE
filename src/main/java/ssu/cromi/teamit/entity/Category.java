package ssu.cromi.teamit.entity;

public enum Category {
    공모전("공모전"),
    대회("대회"),
    사이드_프로젝트("사이드 프로젝트"),
    토이_프로젝트("토이 프로젝트");

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

