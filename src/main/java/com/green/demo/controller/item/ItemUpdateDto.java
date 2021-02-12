package com.green.demo.controller.item;

import lombok.Getter;

@Getter
public class ItemUpdateDto {

    private String itemName;

    private String description;

    private int sellingPrice;

    private int unitSales;

    private String status;
}
