package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.inventory.UserInventoryResponse;

public interface UserInventoryService {
    /**
     * 사용자의 인벤토리 아이템 조회 (배경사진, 테두리, 뱃지)
     */
    UserInventoryResponse getUserInventory(String uid);
}