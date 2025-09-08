package ssu.cromi.teamit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssu.cromi.teamit.DTO.inventory.UserInventoryResponse;
import ssu.cromi.teamit.security.JwtUtils;
import ssu.cromi.teamit.service.UserInventoryService;

@RestController
@RequestMapping("/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final UserInventoryService userInventoryService;
    private final JwtUtils jwtUtils;
    
    @GetMapping
    public ResponseEntity<UserInventoryResponse> getUserInventory(
            @RequestHeader("Authorization") String token) {
        String uid = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        UserInventoryResponse response = userInventoryService.getUserInventory(uid);
        return ResponseEntity.ok(response);
    }
}