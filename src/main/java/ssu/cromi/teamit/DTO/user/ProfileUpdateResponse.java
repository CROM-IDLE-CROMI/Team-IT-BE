package ssu.cromi.teamit.DTO.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateResponse {
    private String nickName;
    private Integer birthday;
    private String email;
    private String introduction;
    private String position;
}