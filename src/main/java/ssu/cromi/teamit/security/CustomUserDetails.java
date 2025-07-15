package ssu.cromi.teamit.security;

// 로그인 연동 전이므로 UserDetails 미구현
public class CustomUserDetails {
    private String userId;

    public CustomUserDetails(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}