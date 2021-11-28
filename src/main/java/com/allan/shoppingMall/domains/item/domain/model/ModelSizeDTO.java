package com.allan.shoppingMall.domains.item.domain.model;

import lombok.*;

/**
 * front <-> server 데이터 전달 object
 */

@Getter
@Setter
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

    @Builder
    public ModelSizeDTO(Double modelShoulderSize, Double modelWaist, Double modelHeap, Double modelHeight, Double modelWeight) {
        this.modelShoulderSize = modelShoulderSize;
        this.modelWaist = modelWaist;
        this.modelHeap = modelHeap;
        this.modelHeight = modelHeight;
        this.modelWeight = modelWeight;
    }

    @Override
    public String toString() {
        return "ModelSize [modelShoulderSize=" + this.modelShoulderSize + ", modelWaist=" + this.modelWaist +
                ", modelHeap=" + this.modelHeap + ", modelHeight=" + this.modelHeight + "]";
    }
}
