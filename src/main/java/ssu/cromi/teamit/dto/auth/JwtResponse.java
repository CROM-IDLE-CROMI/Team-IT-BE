package ssu.cromi.teamit.dto.auth;


import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
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

    public JwtResponse(String newAccess, long jwtExpirationMs, String token) {
    }
}
