package com.green.demo.controller.item;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemUpdateDto {

    private String itemName;

    private String description;

    private int sellingPrice;

    private int unitSales;

    private String status;

    @Builder
    public ItemUpdateDto(String itemName, String description, int sellingPrice,
                         int unitSales, String status) {
        this.itemName = itemName;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.unitSales = unitSales;
        this.status = status;
    }
}
