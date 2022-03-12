package com.allan.shoppingMall.domains.item.domain.accessory;

import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Accessory extends Item {

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccessorySize> accessorySizes = new ArrayList<>();

    @Column(name = "eng_name", nullable = false)
    private String engName;

    @Builder
    public Accessory(String name, Long price, Color color, String engName){
        super(name, price, color);
        this.engName = engName;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * AccessorySize 등록함.
     * @param accessorySizes
     */
    public void changeAccessorySize(List<AccessorySize> accessorySizes){
        long totalQuantity = 0l;
        for(AccessorySize accessorySize : accessorySizes){
            totalQuantity += accessorySize.getStockQuantity();
            this.accessorySizes.add(accessorySize);
            accessorySize.changeItem(this);
        }
        addStockQuantity(totalQuantity); // 재고량을 증가하기 위한 메소드.
    }

    public void changeEngName(String engName){
        this.engName = engName;
    }
}
