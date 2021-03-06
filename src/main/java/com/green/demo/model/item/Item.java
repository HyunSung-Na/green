package com.green.demo.model.item;

import com.green.demo.controller.item.ItemUpdateDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String itemName;

    @Column
    @Enumerated
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
    private int stars;

    @Column
    private String status;

    @Column
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private final List<Review> reviews = new ArrayList<>();

    @Builder
    public Item(String itemName, Name owner, String description, int sellingPrice,
                int unitSales, String status) {
        this(itemName, owner, description, sellingPrice, unitSales, status, null);
    }

    @Builder
    public Item(String itemName, Name owner, String description, int sellingPrice,
                int unitSales, String status, String itemImageUrl) {
        this(null, itemName, owner, description, sellingPrice, unitSales, itemImageUrl, 0, status, null, null);
    }

    @Builder
    public Item(Long id, String itemName, Name owner, String description, int sellingPrice,
                int unitSales, String itemImageUrl, int stars, String status, LocalDateTime createAt, User user) {
        checkArgument(isNotEmpty(itemName), "itemName must be provided.");
        checkArgument(
                itemName.length() >= 2 && itemName.length() <= 50,
                "itemName length must be between 2 and 50 characters."
        );

        checkArgument(isNotEmpty(description), "description must be provided.");
        checkArgument(
                description.length() >= 2 && description.length() <= 1000,
                "description length must be between 2 and 1000 characters."
        );

        this.id = id;
        this.itemName = itemName;
        this.owner = owner;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.unitSales = unitSales;
        this.itemImageUrl = itemImageUrl;
        this.stars = stars;
        this.status = status;
        this.createAt = defaultIfNull(createAt, now());
        this.user = user;
    }

    public void update(ItemUpdateDto updateDto) {

        checkArgument(isNotEmpty(itemName), "itemName must be provided.");
        checkArgument(
                itemName.length() >= 2 && itemName.length() <= 50,
                "itemName length must be between 2 and 50 characters."
        );

        checkArgument(isNotEmpty(description), "description must be provided.");
        checkArgument(
                description.length() >= 2 && description.length() <= 1000,
                "description length must be between 2 and 1000 characters."
        );

        this.itemName = updateDto.getItemName();
        this.description = updateDto.getDescription();
        this.sellingPrice = updateDto.getSellingPrice();
        this.unitSales = updateDto.getUnitSales();
        this.status = updateDto.getStatus();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void addReview(Review review) {
        this.reviews.add(review);
        avgStars();
    }

    private void avgStars() {
        int sumStars = this.reviews.stream().mapToInt(review -> review.getStar().getStar()).sum();
        this.stars = sumStars / this.reviews.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder().append(sellingPrice, item.sellingPrice).append(unitSales, item.unitSales).append(id, item.id).append(itemName, item.itemName).append(owner, item.owner).append(description, item.description).append(itemImageUrl, item.itemImageUrl).append(status, item.status).append(createAt, item.createAt).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(itemName).append(owner).append(description).append(sellingPrice).append(unitSales).append(itemImageUrl).append(status).append(createAt).toHashCode();
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
