package ssu.cromi.teamit.DTO.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProfileUpdateResponse {
    private String nickName;
    private LocalDate birthday;
    private String email;
    private String introduction;
    private String position;
}