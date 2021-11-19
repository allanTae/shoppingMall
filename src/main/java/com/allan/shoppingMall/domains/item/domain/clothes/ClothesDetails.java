package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.Getter;

import javax.persistence.*;

/**
 * 의류 세부 내용을 저장 할 엔티티.
 */

@Entity
@Getter
@Table(name = "clothesDetails")
public class ClothesDetails extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clothesDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 의상 세부 내용.
    @Column(name = "detail_desc")
    private String detailDesc;
}
