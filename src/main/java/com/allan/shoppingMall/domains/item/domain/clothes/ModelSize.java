package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패션 모델의 사이즈 정보를 담는 Value object.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "model_sizes")
public class ModelSize extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ModelSizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 어깨 사이즈.
    @Column(name = "model_shoulder_size")
    private Double modelShoulderSize;

    // 허리 사이즈.
    @Column(name = "model_waist_size")
    private Double modelWaist;

    // 엉덩이 사이즈.
    @Column(name = "model_heap")
    private Double modelHeap;

    // 키 사이즈.
    @Column(name = "model_height")
    private Double modelHeight;

    // 몸부게 사이즈.
    @Column(name = "model_weight")
    private Double  modelWeight;

    @Builder
    public ModelSize(Double modelShoulderSize, Double modelWaist, Double modelHeap, Double modelHeight, Double modelWeight) {
        this.modelShoulderSize = modelShoulderSize;
        this.modelWaist = modelWaist;
        this.modelHeap = modelHeap;
        this.modelHeight = modelHeight;
        this.modelWeight = modelWeight;
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
