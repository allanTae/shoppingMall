package com.allan.shoppingMall.domains.item.domain.model;

import lombok.*;

/**
 * front <-> server 데이터 전달 object
 */

@Getter
@Setter
@NoArgsConstructor
public class ItemDetailDTO {
    private String detailDesc; // 원단 디테일 설명.

    @Builder
    public ItemDetailDTO(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    @Override
    public String toString(){
        return "ItemDetailDTO [detailDesc=" + this.detailDesc + "]";
    }
}
