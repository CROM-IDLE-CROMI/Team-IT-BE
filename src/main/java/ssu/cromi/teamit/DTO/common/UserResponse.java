package ssu.cromi.teamit.DTO.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserResponse {
    @JsonProperty("UID")
    private String uid;

    @JsonProperty("email")
    private String email;

    @JsonProperty("nickName")
    private String nickName;

    @JsonProperty("Created_at")
    private String createdAt;

    @JsonProperty("BirthDay")
    private Integer birthday;

    public UserResponse(String uid, String email, String nickName, String createdAt, Integer birthday) {
        this.uid = uid;
        this.email = email;
        this.nickName = nickName;
        this.createdAt = createdAt;
        this.birthday = birthday;
    }
}