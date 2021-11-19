package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.Getter;

import javax.persistence.*;

/**
 * 의류에 원단 정보를 저장 할 엔티티.
 */

@Entity
@Getter
@Table(name = "clothesFabrics")
public class ClothesFabrics extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clothesFabricId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 원단 부위.
    @Column(name = "materail_part")
    private String materialPart;

    // 원단 설명.
    @Column(name = "material_desc")
    private String materialDesc;
}
