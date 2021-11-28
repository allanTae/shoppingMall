package com.allan.shoppingMall.domains.item.domain.model;

import lombok.*;

/**
 * front <-> server 데이터 전달 object
 */

@Getter
@Setter
@NoArgsConstructor
public class ClothesSizeDTO {

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

    @Builder
    public ClothesSizeDTO(String sizeLabel, Double backLength, Double chestWidth, Double shoulderWidth, Double sleeveLength, Double waistWidth, Double heapWidth, Double bottomWidth) {
        this.sizeLabel = sizeLabel;
        this.backLength = backLength;
        this.chestWidth = chestWidth;
        this.shoulderWidth = shoulderWidth;
        this.sleeveLength = sleeveLength;
        this.waistWidth = waistWidth;
        this.heapWidth = heapWidth;
        this.bottomWidth = bottomWidth;
    }

    @Override
    public String toString() {
        return "ClothesSized [clothesSize=" + this.sizeLabel + ", backLength=" + this.backLength + ", chestWidth=" + this.chestWidth +
                ", shoulderWidth=" + this.shoulderWidth + ", sleeveLength=" + this.sleeveLength + ", waistWidth=" + this.waistWidth +
                ", heapWidth=" + this.heapWidth + ", bottomWidth=" + this.bottomWidth + "]";
    }
}
