package ssu.cromi.teamit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;
    
    @Column(name = "item_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    
    @Column(name = "item_url", nullable = false, length = 2083)
    private String itemUrl;
    
    @Column(name = "description", length = 500)
    private String description;
    
    public enum ItemType {
        BACKGROUND_IMAGE,
        BORDER,
        BADGE
    }
}