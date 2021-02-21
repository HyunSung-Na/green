package com.green.demo.repository.item;

import com.green.demo.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemName(String itemName);
}
