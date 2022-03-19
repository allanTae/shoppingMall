package com.allan.shoppingMall.domains.item.domain.item;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderFailException;
import com.allan.shoppingMall.domains.category.domain.CategoryItem;
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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFabric> itemFabrics = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDetail> itemDetails = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemSize> itemSizes = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    public Item(String name, Long price, Color color) {
        this.name = name;
        this.price = price;
        this.color = color;
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
     * 재고량 차감 메소드.
     * @param stockQuantity
     */
    public void subtractStockQuantity(Long stockQuantity){
        if(this.stockQuantity < stockQuantity) {
            throw new OrderFailException(ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED.getMessage(), ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED);
        }
        this.stockQuantity -= stockQuantity;
    }

    /**
     * 재고량 증가 메소드.
     * @param
     * @return
     */
    public void addStockQuantity(Long stockQuantity){
        this.stockQuantity += stockQuantity;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * itemFabric 등록함.
     * @param itemFabrics
     */
    public void changeItemFabrics(List<ItemFabric> itemFabrics){
        for(ItemFabric itemFabric : itemFabrics){
            this.itemFabrics.add(itemFabric);
            itemFabric.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * itemDetail 등록함.
     * @param itemDetails
     */
    public void changeItemDetails(List<ItemDetail> itemDetails){
        for(ItemDetail itemDetail : itemDetails){
            this.itemDetails.add(itemDetail);
            itemDetail.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * itemSize 등록함.
     * 사이즈별로 수량을 관리 해야 하기 때문에 ItemSize에 모든 수량이 곧 clothes 상품의 재고량이 된다.
     * @param itemSizes
     */
    public void changeItemSizes(List<ItemSize> itemSizes){
        long totalQuantity = 0l;
        for(ItemSize itemSize : itemSizes){
            totalQuantity += itemSize.getStockQuantity();
            this.itemSizes.add(itemSize);
            itemSize.changeItem(this);
        }
        addStockQuantity(totalQuantity); // 재고량을 증가하기 위한 메소드.
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * 카테고리 상품 도메인 추가를 위한 메소드 입니다.
     * @param categoryItems
     */
    public void changeCategoryItems(List<CategoryItem> categoryItems){
        for(CategoryItem categoryItem : categoryItems){
            if(!this.categoryItems.contains(categoryItem)){
                this.categoryItems.add(categoryItem);
                categoryItem.changeitem(this);
            }
        }
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changePrice(Long price){
        this.price = price;
    }

    public void changeColor(Color color){
        this.color = color;
    }

}
