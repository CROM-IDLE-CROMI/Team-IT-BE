package ssu.cromi.teamit.domain;

public enum StackLevel {
    high("상"),
    middle("중"),
    low("하"),
    none("없음");
    
    private final String displayName;
    
    StackLevel(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}