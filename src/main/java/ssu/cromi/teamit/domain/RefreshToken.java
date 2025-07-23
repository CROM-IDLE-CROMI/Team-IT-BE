package ssu.cromi.teamit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Setter
    @Id
    @GeneratedValue
    private long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    User user;

    @Setter
    @Column(nullable = false, unique = true)
    private String token;


    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    protected RefreshToken(){}

    public RefreshToken(User user, String token, Instant expiryDate){
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

}
