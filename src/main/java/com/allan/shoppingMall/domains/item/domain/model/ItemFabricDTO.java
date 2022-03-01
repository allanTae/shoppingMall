package com.allan.shoppingMall.domains.item.domain.model;

import lombok.*;

/**
 * front <-> server 데이터 전달 object.
 */

@Getter
@Setter
@NoArgsConstructor
public class ItemFabricDTO {
    private String materialPart; // 원단 부위 정보.
    private String materialDesc; // 원단 설명.

    @Builder
    public ItemFabricDTO(String materialPart, String materialDesc) {
        this.materialPart = materialPart;
        this.materialDesc = materialDesc;
    }

    @Override
    public String toString(){
        return "ItemFabricDTO [materialPart=" + this.materialPart + ", materialDesc=" + this.getMaterialDesc() + "]";
    }
}
