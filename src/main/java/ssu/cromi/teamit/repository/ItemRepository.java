package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemType(Item.ItemType itemType);
}