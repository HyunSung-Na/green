package com.green.demo.controller.item;

import com.green.demo.model.Name;
import com.green.demo.model.item.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemCreateDto {

    private String itemName;

    private Name owner;

    private String description;

    private int sellingPrice;

    private int unitSales;

    private String status;

    @Builder
    public ItemCreateDto(String itemName, Name owner, String description,
                         int sellingPrice, int unitSales, String status) {
        this.itemName = itemName;
        this.owner = owner;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.unitSales = unitSales;
        this.status = status;
    }

    public Item of() {
        return Item.builder()
                .itemName(itemName)
                .description(description)
                .owner(owner)
                .sellingPrice(sellingPrice)
                .unitSales(unitSales)
                .status(status)
                .build();
    }
}
