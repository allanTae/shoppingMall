package com.allan.shoppingMall.domains.item.domain.accessory;

import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Accessory extends Item {

    @Column(name = "eng_name", nullable = false)
    private String engName;

    @Builder
    public Accessory(String name, Long price, Color color, String engName, Category category){
        super(name, price, color, category);
        this.engName = engName;
    }
}
