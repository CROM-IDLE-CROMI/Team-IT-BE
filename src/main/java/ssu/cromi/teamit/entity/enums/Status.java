package ssu.cromi.teamit.entity.enums;

public enum Status {
    RECRUITING("팀원 모집 중"),
    ADDITIONAL("추가 모집 중"),
    CLOSED("모집 완료"),
    ONGOING("프로젝트 진행중"),
    FINISHED("프로젝트 종료"),
    DELETED("삭제된 프로젝트");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}