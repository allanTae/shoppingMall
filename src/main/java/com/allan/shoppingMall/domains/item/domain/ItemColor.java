package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item_color")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemColor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemColorId;

    @Column(name = "color", nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemColor(String color) {
        this.color = color;
    }

    /**
     * 관계 매핑을 위한 연관관계 편의 메소드.
     */
    public void changeItem(Item item){
        this.item = item;
    }
}
