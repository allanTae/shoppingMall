package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 의류상품 사이즈 도메인.
 * 의류상품 사이즈 정보를 저장하는 엔티티 입니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class ClothesSize extends ItemSize {

    // 총장 사이즈.
    @Column(name = "back_length")
    private Double backLength;

    // 가슴 둘레 사이즈.
    @Column(name = "chest_width")
    private Double chestWidth;

    // 어깨 넓이 사이즈.
    @Column(name = "shoulder_width")
    private Double shoulderWidth;

    // 소매 길이 사이즈.
    @Column(name = "sleeve_length")
    private Double sleeveLength;

    // 허리 둘레 사이즈.
    @Column(name = "waist_width")
    private Double waistWidth;

    // 엉덩이 사이즈.
    @Column(name = "heap_width")
    private Double heapWidth;

    // 밑단 둘레 사이즈.
    @Column(name = "bottom_width")
    private Double bottomWidth;

    @Builder
    public ClothesSize(SizeLabel sizeLabel, Double backLength, Double chestWidth, Double shoulderWidth, Double sleeveLength, Double waistWidth, Double heapWidth, Double bottomWidth, Long stockQuantity) {
        super(sizeLabel, stockQuantity);
        this.backLength = backLength;
        this.chestWidth = chestWidth;
        this.shoulderWidth = shoulderWidth;
        this.sleeveLength = sleeveLength;
        this.waistWidth = waistWidth;
        this.heapWidth = heapWidth;
        this.bottomWidth = bottomWidth;
    }

    /**
     * 그외 사이즈 정보를 수정하는 메소드.
     * @param sizeLabel
     * @param backLength
     * @param chestWidth
     * @param shoulderWidth
     * @param sleeveLength
     * @param waistWidth
     * @param heapWidth
     * @param bottomWidth
     */
    public void updateClothesSizeInfo(SizeLabel sizeLabel, Double backLength, Double chestWidth, Double shoulderWidth, Double sleeveLength, Double waistWidth, Double heapWidth, Double bottomWidth){
        this.updateSizeLabel(sizeLabel);
        this.backLength = backLength;
        this.chestWidth = chestWidth;
        this.shoulderWidth = shoulderWidth;
        this.sleeveLength = sleeveLength;
        this.waistWidth = waistWidth;
        this.heapWidth = heapWidth;
        this.bottomWidth = bottomWidth;
    }
}
