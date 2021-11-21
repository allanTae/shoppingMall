package com.allan.shoppingMall.domains.item.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClothesDetailsDTO {
    private String detailDesc; // 원단 디테일 설명.

    @Override
    public String toString(){
        return "ClothesDetails [detailDesc=" + this.detailDesc + "]";
    }
}
