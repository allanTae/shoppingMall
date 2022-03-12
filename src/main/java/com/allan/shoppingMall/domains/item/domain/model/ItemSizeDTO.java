package com.allan.shoppingMall.domains.item.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
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

    // 수정 폼, 사이즈 라벨 option tag 를 위한 필드.
    // 수정 폼에서(clothesForm.jsp, accessoryForm.jsp 에서 select tag 내 option tag 에서 사용할 사이즈 정보).
    private SizeLabel labelInfo;

    public ItemSizeDTO(String sizeLabel, Long stockQuantity) {
        this.sizeLabel = sizeLabel;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "ItemSizeDTO [sizeLabel=" + this.sizeLabel +  ", sizeQuantity=" + this.stockQuantity + "]";
    }
}
