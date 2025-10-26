package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssu.cromi.teamit.entity.Item;
import ssu.cromi.teamit.entity.UserInventory;

import java.util.List;

public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {
    
    List<UserInventory> findByUserUidAndStatus(String uid, UserInventory.InventoryStatus status);

    @Query("SELECT ui FROM UserInventory ui JOIN ui.item i WHERE ui.user.uid = :uid AND ui.status = :status AND i.itemType = :itemType")
    List<UserInventory> findByUidAndStatusAndItemType(
            @Param("uid") String uid,
            @Param("status") UserInventory.InventoryStatus status,
            @Param("itemType") Item.ItemType itemType);

    @Query("SELECT ui FROM UserInventory ui JOIN ui.item i WHERE ui.user.uid = :uid AND ui.status = 'ACTIVE' AND i.itemType IN :itemTypes")
    List<UserInventory> findByUidAndActiveStatusAndItemTypes(
            @Param("uid") String uid,
            @Param("itemTypes") List<Item.ItemType> itemTypes);
}