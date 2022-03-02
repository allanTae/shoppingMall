package com.allan.shoppingMall.domains.item.domain.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * front <-> server 데이터 전달 object
 */

@Getter
@Setter
@NoArgsConstructor
public class ItemSizeDTO {

    // 의류 사이즈.
    private String sizeLabel;

    // 사이즈별 재고량.
    private Long stockQuantity;

    public ItemSizeDTO(String sizeLabel, Long quantity) {
        this.sizeLabel = sizeLabel;
        this.stockQuantity = quantity;
    }

    @Override
    public String toString() {
        return "ItemSizeDTO [sizeLabel=" + this.sizeLabel +  ", sizeQuantity=" + this.stockQuantity + "]";
    }
}
