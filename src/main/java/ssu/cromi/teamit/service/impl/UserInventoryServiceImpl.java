package ssu.cromi.teamit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.inventory.InventoryItemResponse;
import ssu.cromi.teamit.DTO.inventory.UserInventoryResponse;
import ssu.cromi.teamit.entity.Item;
import ssu.cromi.teamit.entity.UserInventory;
import ssu.cromi.teamit.repository.UserInventoryRepository;
import ssu.cromi.teamit.service.UserInventoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInventoryServiceImpl implements UserInventoryService {
    
    private final UserInventoryRepository userInventoryRepository;
    
    @Override
    public UserInventoryResponse getUserInventory(String uid) {
        // 사용자의 ACTIVE 상태인 배경사진, 테두리, 뱃지 아이템들을 조회
        List<Item.ItemType> targetTypes = Arrays.asList(
                Item.ItemType.BACKGROUND_IMAGE,
                Item.ItemType.BORDER,
                Item.ItemType.BADGE
        );
        
        List<UserInventory> inventoryItems = userInventoryRepository
                .findByUidAndActiveStatusAndItemTypes(uid, targetTypes);
        
        // 아이템 타입별로 그룹화
        Map<Item.ItemType, List<InventoryItemResponse>> groupedItems = inventoryItems.stream()
                .collect(Collectors.groupingBy(
                        inventory -> inventory.getItem().getItemType(),
                        Collectors.mapping(this::convertToResponse, Collectors.toList())
                ));
        
        return UserInventoryResponse.builder()
                .backgroundImages(groupedItems.getOrDefault(Item.ItemType.BACKGROUND_IMAGE, List.of()))
                .borders(groupedItems.getOrDefault(Item.ItemType.BORDER, List.of()))
                .badges(groupedItems.getOrDefault(Item.ItemType.BADGE, List.of()))
                .build();
    }
    
    private InventoryItemResponse convertToResponse(UserInventory inventory) {
        return InventoryItemResponse.builder()
                .inventoryId(inventory.getId())
                .itemId(inventory.getItem().getId())
                .itemName(inventory.getItem().getItemName())
                .itemType(inventory.getItem().getItemType())
                .itemUrl(inventory.getItem().getItemUrl())
                .description(inventory.getItem().getDescription())
                .status(inventory.getStatus().name())
                .expiresAt(inventory.getExpiresAt())
                .build();
    }
}