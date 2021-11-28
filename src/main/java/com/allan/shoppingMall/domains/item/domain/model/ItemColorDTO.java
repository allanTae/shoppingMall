package com.allan.shoppingMall.domains.item.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * front <-> server 데이터 전달 object/.
 */

@Getter
@Setter
@NoArgsConstructor
public class ItemColorDTO {
    String color;

    @Builder
    public ItemColorDTO(String color) {
        this.color = color;
    }

    public String toString(){
        return "ItemColor [color=" + this.color + "]";
    }
}
