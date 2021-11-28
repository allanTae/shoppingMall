package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.OrderFailException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "items")
@NoArgsConstructor
@Slf4j
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemColor> itemColors = new ArrayList<>();

    @Column(nullable = false, name = "stock_quantity")
    private Long stockQuantity;

    public Item(String name, Long price, Long stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesFabric 등록함.
     * @param itemImages
     */
    public void changeItemImages(List<ItemImage> itemImages){
        for(ItemImage itemImage : itemImages){
            this.itemImages.add(itemImage);
            itemImage.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * @param itemColors
     */
    public void changeItemColors(List<ItemColor> itemColors){
        for(ItemColor color : itemColors){
            this.itemColors.add(color);
            color.changeItem(this);
        }
    }

    public void subtractStockQuantity(Long orderQuantity){
        if(this.stockQuantity < orderQuantity)
            throw new OrderFailException(ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED.getMessage(), ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED);

        this.stockQuantity -= orderQuantity;
    }

    public void addStockQuantity(Long orderQuantity){
        this.stockQuantity += orderQuantity;
    }
}
