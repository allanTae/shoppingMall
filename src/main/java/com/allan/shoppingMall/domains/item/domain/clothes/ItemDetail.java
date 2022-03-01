package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 의류 세부 내용을 저장 할 엔티티.
 */

@Entity
@Getter
@Table(name = "item_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetail extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 의상 세부 내용.
    @Column(name = "detail_desc")
    private String detailDesc;

    @Builder
    public ItemDetail(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * Item Entity 측에서 ClothesFabric 추가하도록 비즈니스 로직을 처리함.
     * @param item
     */
    public void changeItem(Item item){
        this.item = item;
    }
}
