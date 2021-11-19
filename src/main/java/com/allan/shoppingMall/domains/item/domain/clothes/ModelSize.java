package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.Getter;

import javax.persistence.*;

/**
 * 패션 모델의 사이즈 정보를 담는 Value object.
 */
@Entity
@Getter
public class ModelSize extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ModelSizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 어깨 사이즈.
    @Column(name = "model_shoulder_size")
    private String modelShoulderSize;

    // 허리 사이즈.
    @Column(name = "model_waist_size")
    private String modelWaist;

    // 엉덩이 사이즈.
    @Column(name = "model_heap")
    private String modelHeap;

    // 키 사이즈.
    @Column(name = "model_height")
    private String modelHeight;
}
