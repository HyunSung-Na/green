package com.green.demo.model.item;

import com.green.demo.controller.item.ItemUpdateDto;
import com.green.demo.model.Name;
import com.green.demo.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column
    private String itemName;

    @Column
    private Name owner;

    @Column
    private String description;

    @Column
    private int sellingPrice;

    @Column
    private int unitSales;

    @Column
    private String itemImageUrl;

    @Column
    private String status;

    @Column
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Item(String itemName, Name owner, String description, int sellingPrice,
                int unitSales, String status) {
        this(itemName, owner, description, sellingPrice, unitSales, status, null);
    }

    @Builder
    public Item(String itemName, Name owner, String description, int sellingPrice,
                int unitSales, String status, String itemImageUrl) {
        this(null, itemName, owner, description, sellingPrice, unitSales, itemImageUrl, status, null);
    }

    @Builder
    public Item(Long seq, String itemName, Name owner, String description, int sellingPrice,
                int unitSales, String itemImageUrl, String status, LocalDateTime createAt) {
        this.seq = seq;
        this.itemName = itemName;
        this.owner = owner;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.unitSales = unitSales;
        this.itemImageUrl = itemImageUrl;
        this.status = status;
        this.createAt = defaultIfNull(createAt, now());;
    }

    public void update(ItemUpdateDto updateDto) {
        this.itemName = updateDto.getItemName();
        this.description = updateDto.getDescription();
        this.sellingPrice = updateDto.getSellingPrice();
        this.unitSales = updateDto.getUnitSales();
        this.status = updateDto.getStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder().append(sellingPrice, item.sellingPrice).append(unitSales, item.unitSales).append(seq, item.seq).append(itemName, item.itemName).append(owner, item.owner).append(description, item.description).append(itemImageUrl, item.itemImageUrl).append(status, item.status).append(createAt, item.createAt).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(seq).append(itemName).append(owner).append(description).append(sellingPrice).append(unitSales).append(itemImageUrl).append(status).append(createAt).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("itemName", itemName)
                .append("owner", owner)
                .append("description", description)
                .append("sellingPrice", sellingPrice)
                .append("unitSales", unitSales)
                .append("itemImageUrl", itemImageUrl)
                .append("status", status)
                .append("createAt", createAt)
                .toString();
    }


}
