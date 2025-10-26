package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssu.cromi.teamit.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    
    @Builder.Default
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private InventoryStatus status = InventoryStatus.ACTIVE;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    public enum InventoryStatus {
        ACTIVE,
        USED,
        EXPIRED
    }
}