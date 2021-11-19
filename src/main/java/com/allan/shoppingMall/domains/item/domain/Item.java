package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "items")
@NoArgsConstructor
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @OneToMany(mappedBy = "item")
    private List<ItemImage> itemMainImages = new ArrayList<>();

    @Column(nullable = false, name = "stock_quantity")
    private Long stockQuantity;

    public Item(String name, Long price, Long stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
