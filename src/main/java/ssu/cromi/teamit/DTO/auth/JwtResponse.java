package ssu.cromi.teamit.DTO.auth;


import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class JwtResponse {
    @ToString.Exclude // accessToken은 toString, Equals 제외
    @EqualsAndHashCode.Exclude
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;

    @ToString.Exclude // refreshToken은 toString, Equals 제외
    @EqualsAndHashCode.Exclude
    private String refreshToken;

    private String uid;
}
