package com.allan.shoppingMall.domains.item.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 의류 상품 사이즈 정보를 담고있는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ClothesSizeDTO extends ItemSizeDTO {

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
    public ClothesSizeDTO(String sizeLabel, Double backLength, Double chestWidth, Double shoulderWidth, Double sleeveLength, Double waistWidth, Double heapWidth, Double bottomWidth, Long quantity) {
        super(sizeLabel, quantity);
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
        return "ClothesSizeDTO{" +
                "sizeLabel=" + getSizeLabel() +
                "stockQuantity= " + getStockQuantity() +
                "backLength=" + backLength +
                ", chestWidth=" + chestWidth +
                ", shoulderWidth=" + shoulderWidth +
                ", sleeveLength=" + sleeveLength +
                ", waistWidth=" + waistWidth +
                ", heapWidth=" + heapWidth +
                ", bottomWidth=" + bottomWidth +
                '}';
    }
}
