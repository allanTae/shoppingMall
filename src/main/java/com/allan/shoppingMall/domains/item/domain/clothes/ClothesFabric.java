package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 의류에 원단 정보를 저장 할 엔티티.
 */

@Entity
@Getter
@Table(name = "clothesFabrics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesFabric extends BaseEntity {

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

    @Builder
    public ClothesFabric(String materialPart, String materialDesc) {
        this.materialPart = materialPart;
        this.materialDesc = materialDesc;
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
