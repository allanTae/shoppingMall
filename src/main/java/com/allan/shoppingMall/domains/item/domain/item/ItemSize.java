package com.allan.shoppingMall.domains.item.domain.item;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderFailException;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * 상품 사이즈 도메인.
 * 상품 사이즈 정보를 저장하는 엔티티 입니다.
 */
@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Table(name = "itemSizes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ItemSize extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemSizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 사이즈.
    @Column(name = "clothes_size")
    @Enumerated(value = EnumType.STRING)
    private SizeLabel sizeLabel;

    // 사이즈 별 재고량.
    @Column(name = "size_stock_quantiy")
    private Long stockQuantity;

    public ItemSize(SizeLabel sizeLabel, Long stockQuantity) {
        this.sizeLabel = sizeLabel;
        this.stockQuantity = stockQuantity;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * Item Entity 측에서 ClothesFabric 추가하도록 비즈니스 로직을 처리함.
     * @param
     */
    public void changeItem(Item item){
        this.item = item;
    }

    /**
     * 재고량 추가를 위한 메소드.
     */
    public void addStockQuantity(Long quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고량 감소를 위한 메소드.
     */
    public void subStockQuantity(Long quantity){
        if(this.stockQuantity < quantity) {
            log.error("stockQuantity: " + this.stockQuantity);
            log.error("quantity: " + quantity);
            throw new OrderFailException(ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED.getMessage(), ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED);
        }
        this.stockQuantity -= quantity;
    }
}
