package com.allan.shoppingMall.domains.item.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClothesSizesDTO {

    // 의류 사이즈.
    private String sizeLabel;

    // 총장 사이즈.
    private Double backLength;

    // 가슴 둘레 사이즈.
    private Double chestWidth;

    // 어깨 넓이 사이즈.
    private Double shoulderWidth;

    // 소매 길이 사이즈.
    private Double sleeveLength;

    // 허리 둘레 사이즈.
    private Double waistWidth;

    // 엉덩이 사이즈.
    private Double heapWidth;

    // 밑단 둘레 사이즈.
    private Double bottomWidth;

    @Override
    public String toString() {
        return "ClothesSized [clothesSize=" + this.sizeLabel + ", backLength=" + this.backLength + ", chestWidth=" + this.chestWidth +
                ", shoulderWidth=" + this.shoulderWidth + ", sleeveLength=" + this.sleeveLength + ", waistWidth=" + this.waistWidth +
                ", heapWidth=" + this.heapWidth + ", bottomWidth=" + this.bottomWidth + "]";
    }
}
