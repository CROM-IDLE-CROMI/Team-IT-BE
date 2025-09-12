package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.domain.StackLevel;
import ssu.cromi.teamit.domain.User;

@Data
@Entity
@Table(name = "user_stack")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    private Stack stack;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "stack_level")
    private StackLevel level;
    
    @Column(name = "is_representative")
    private Boolean isRepresentative = false;
}