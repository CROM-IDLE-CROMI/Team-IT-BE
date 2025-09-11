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
    
    @Column(name = "uid", nullable = false, length = 50)
    private String uid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
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