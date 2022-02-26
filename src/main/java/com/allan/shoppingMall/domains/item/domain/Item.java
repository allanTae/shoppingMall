package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderFailException;
import com.allan.shoppingMall.domains.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Table(name = "items")
@NoArgsConstructor
@Slf4j
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @JoinColumn(name = "category_id", nullable = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name="item_color") // 나중에 nullable 처리 필요.
    private Color color;

    @Column(nullable = false, name = "stock_quantity")
    private Long stockQuantity = 0l;

    public Item(String name, Long price, Long stockQuantity, Color color, Category category) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.color = color;
        this.category = category;
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

    public void subtractStockQuantity(Long orderQuantity){

        if(this.stockQuantity < orderQuantity) {
            log.error("stockQuantity: " + this.stockQuantity);
            log.error("orderQuantity: " + orderQuantity);
            throw new OrderFailException(ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED.getMessage(), ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED);
        }
        this.stockQuantity -= orderQuantity;
    }

    public void addStockQuantity(Long orderQuantity){
        this.stockQuantity += orderQuantity;
    }
}
