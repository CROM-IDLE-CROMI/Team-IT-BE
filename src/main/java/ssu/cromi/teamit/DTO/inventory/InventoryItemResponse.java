package ssu.cromi.teamit.DTO.inventory;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.Item;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryItemResponse {
    private Long inventoryId;
    private Long itemId;
    private String itemName;
    private Item.ItemType itemType;
    private String itemUrl;
    private String description;
    private String status;
    private LocalDateTime expiresAt;
}