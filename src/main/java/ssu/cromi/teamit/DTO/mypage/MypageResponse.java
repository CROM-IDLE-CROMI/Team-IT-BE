package ssu.cromi.teamit.DTO.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MypageResponse {
    private String nickName;
    private LocalDateTime birthDay;
    private String organization;
    private String email;
    private String position;
    private String description;
    private List<String> projects;
    private List<StackWithLevel> stacks;
    private String prize;
    private double stars;
}
