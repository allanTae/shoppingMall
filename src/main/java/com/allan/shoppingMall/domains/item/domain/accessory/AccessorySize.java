package com.allan.shoppingMall.domains.item.domain.accessory;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 악세서리상품 사이즈 도메인.
 * 악세서리상품 사이즈 정보를 저장하는 엔티티 입니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccessorySize extends ItemSize {

    @Column(name = "width_length")
    private String widthLength;

    @Column(name = "height_length")
    private String heightLength;

    @Builder
    public AccessorySize(SizeLabel sizeLabel, Long stockQuantity, String widthLength, String heightLength){
        super(sizeLabel, stockQuantity);
        this.widthLength = widthLength;
        this.heightLength = heightLength;
    }
}
