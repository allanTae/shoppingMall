package com.allan.shoppingMall.domains.item.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 악세서리 상품 사이즈 정보를 담고있는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessorySizeDTO extends ItemSizeDTO{

    // 너비.
    private Double widthLength;

    // 높이.
    private Double heightLength;

    @Builder
    public AccessorySizeDTO(String sizeLabel, Long stockQuantity, Double widthLength, Double heightLength, SizeLabel labelInfo){
        super(sizeLabel, stockQuantity);
        this.widthLength = widthLength;
        this.heightLength = heightLength;
        setLabelInfo(labelInfo);
    }

    @Override
    public String toString() {
        return "AccessorySizeDTO{" +
                "sizeLabel='" + getSizeLabel() + '\'' +
                "stockQuantity='" + getStockQuantity() + '\'' +
                "widthLength='" + widthLength + '\'' +
                ", heightLength='" + heightLength + '\'' +
                '}';
    }
}
