package com.green.demo.controller.item;

import com.green.demo.model.common.Name;
import com.green.demo.model.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ItemDto {

    private Long seq;

    private String itemName;

    private Name owner;

    private String description;

    private int sellingPrice;

    private int unitSales;

    private String itemImageUrl;

    private String status;

    private LocalDateTime createAt;

    @Builder
    private ItemDto(Long seq, String itemName, Name owner, String description, int sellingPrice,
                   int unitSales, String itemImageUrl, String status, LocalDateTime createAt) {
        this.seq = seq;
        this.itemName = itemName;
        this.owner = owner;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.unitSales = unitSales;
        this.itemImageUrl = itemImageUrl;
        this.status = status;
        this.createAt = createAt;
    }

    public static ItemDto of(Item item) {

        return ItemDto.builder()
                .seq(item.getSeq())
                .itemName(item.getItemName())
                .owner(item.getOwner())
                .description(item.getDescription())
                .sellingPrice(item.getSellingPrice())
                .unitSales(item.getUnitSales())
                .itemImageUrl((item.getItemImageUrl() == null) ? null : item.getItemImageUrl())
                .status(item.getStatus())
                .createAt(item.getCreateAt())
                .build();
    }
}
