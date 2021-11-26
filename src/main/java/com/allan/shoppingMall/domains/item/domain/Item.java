package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
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
        log.info("Item Entity's changeItemImages() call");
        for(ItemImage itemImage : itemImages){
            this.itemImages.add(itemImage);
            itemImage.changeItem(this);
        }
    }
}
