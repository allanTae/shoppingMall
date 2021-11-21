package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

/**
 * 의류 사이즈 정보를 저장 할 엔티티.
 */
@Entity
@Getter
@Table(name = "clothesSizes")
public class ClothesSize extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clothesSizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 의류 사이즈.
    @Column(name = "clothes_size")
    @Enumerated(value = EnumType.STRING)
    private SizeLabel sizeLabel;

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
    public ClothesSize(SizeLabel sizeLabel, Double backLength, Double chestWidth, Double shoulderWidth, Double sleeveLength, Double waistWidth, Double heapWidth, Double bottomWidth) {
        this.sizeLabel = sizeLabel;
        this.backLength = backLength;
        this.chestWidth = chestWidth;
        this.shoulderWidth = shoulderWidth;
        this.sleeveLength = sleeveLength;
        this.waistWidth = waistWidth;
        this.heapWidth = heapWidth;
        this.bottomWidth = bottomWidth;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * Item Entity 측에서 ClothesFabric 추가하도록 비즈니스 로직을 처리함.
     * @param
     */
    public void changeItem(Item item){
        this.item = item;
    }
}
