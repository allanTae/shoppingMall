package com.allan.shoppingMall.domains.item.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelSizeDTO {
    // 어깨 사이즈.
    private Double modelShoulderSize;

    // 허리 사이즈.
    private Double modelWaist;

    // 엉덩이 사이즈.
    private Double modelHeap;

    // 키 사이즈.
    private Double modelHeight;

    // 몸무게 사이즈.
    private Double modelWeight;

    @Override
    public String toString() {
        return "ModelSize [modelShoulderSize=" + this.modelShoulderSize + ", modelWaist=" + this.modelWaist +
                ", modelHeap=" + this.modelHeap + ", modelHeight=" + this.modelHeight + "]";
    }
}
