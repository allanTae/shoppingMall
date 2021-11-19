package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.Getter;

import javax.persistence.*;

/**
 * 의류 사이즈 정보를 저장 할 엔티티.
 */
@Entity
@Getter
@Table(name = "clothesSizes")
public class ClothesSizes extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clothesSizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 의류 사이즈.
    @Column(name = "clothes_size")
    private String clothesSize;

    // 총장 사이즈.
    @Column(name = "back_length")
    private String backLength;

    // 가슴 둘레 사이즈.
    @Column(name = "chest_width")
    private String chestWidth;

    // 어깨 넓이 사이즈.
    @Column(name = "shoulder_width")
    private String shoulderWidth;

    // 소매 길이 사이즈.
    @Column(name = "sleeve_length")
    private String sleeveLength;

    // 허리 둘레 사이즈.
    @Column(name = "waist_width")
    private String waistWidth;

    // 엉덩이 사이즈.
    @Column(name = "heap_width")
    private String heapWidth;

    // 밑단 둘레 사이즈.
    @Column(name = "bottom_width")
    private String bottomWidth;
}
