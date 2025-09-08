package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.domain.User;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_awards")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "award_name", nullable = false)
    private String awardName;
    
    @Column(name = "organization")
    private String organization;
    
    @Column(name = "award_date")
    private LocalDate awardDate;
    
    @Column(name = "description", length = 1000)
    private String description;
}