package com.allan.shoppingMall.domains.item.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClothesFabricsDTO {
    private String materialPart; // 원단 부위 정보.
    private String materialDesc; // 원단 설명.

    @Override
    public String toString(){
        return "ClothesFabrics [materialPart=" + this.materialPart + ", materialDesc=" + this.getMaterialDesc() + "]";
    }
}
