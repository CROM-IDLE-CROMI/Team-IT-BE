package ssu.cromi.teamit.DTO.inventory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserInventoryResponse {
    private List<InventoryItemResponse> backgroundImages;
    private List<InventoryItemResponse> borders;
    private List<InventoryItemResponse> badges;
}