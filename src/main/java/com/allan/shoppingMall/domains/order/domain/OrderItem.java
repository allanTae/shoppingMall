package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.exception.OrderFailException;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * 주문한 하나의 상품에 대한 정보를 저장하는 Entity.
 * Order, Item 간에 n:m 매핑을 해결하기 위한 중간 테이블.
 * Order 1<->N OrderItem N<->1 Item 관계를 가짐.
 */

@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column(name = "order_quantity", nullable = false)
    private Long orderQuantity;

    @Column(name = "order_item_amount", nullable = false)
    private Long orderItemAmount; // 주문 상품 가격.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public OrderItem(Long orderQuantity, Long orderItemAmount, Item item) {
        this.orderQuantity = orderQuantity;
        this.orderItemAmount = orderItemAmount;
        this.item = setItem(item);
    }

    private Item setItem(Item item) throws OrderFailException {
        item.subtractStockQuantity(this.orderQuantity);
        return item;
    }

    /**
     * 주문 상품의 가격을 계산하는 메소드.
     */
    public void calculateAmount(){
        this.orderItemAmount = this.orderQuantity * this.item.getPrice();
    }

    /**
     * 상품의 재고량을 수정하는 메소드.
     */
    public void cancelOrderItem(){
        this.item.addStockQuantity(this.orderQuantity);
    }

    /**
     * 연관관계 편의 메소드.
     * @param order
     */
    public void changeOrder(Order order){
        this.order = order;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderItemId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!(obj instanceof OrderItem))
            return false;

        return this.orderItemId == ((OrderItem) obj).orderItemId;
    }

}
