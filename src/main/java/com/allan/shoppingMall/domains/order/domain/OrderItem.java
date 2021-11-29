package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.exception.order.OrderFailException;
import com.allan.shoppingMall.domains.item.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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

    public OrderItem(Long orderQuantity, Item item) {
        this.orderQuantity = orderQuantity;
        this.item = setItem(item);
        calculateAmount();
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
     * 주문 취소에 따
     * 상품의 재고량을 수정하는 메소라드.
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
        return Objects.hashCode(this.item.getItemId());
    }

    /**
     * 주문시, 동일한 주문상품 도메인이 들어가는 것을 방지하기 위한 비교함수.
     * 하나의 주문의 주문상품 도메인에는 동일한 상품이 들어가지 않는다.
     * 그렇기에 비교값으로 상품도메인의 고유 아이디로 비교를 한다.
     * (주문 도메인을 통해 주문상품 도메인이 만들어지고,주문 도메인 persist 시점에 주문상품 도메인이
     * 같이 persist 되기 때문에 주문상품 도메인의 고유 아이디로 비교하지 않는다.)
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        log.info("orderItems equals() call!!!");
        if(obj == null){
            log.error("OrderItem equals()'s parameter is null");
            return false;
        }
        if(this == obj)
            return true;
        if(!(obj instanceof OrderItem))
            return false;

        log.info("this.orderItemId: " + this.orderItemId);
        log.info("obj orderItemId:" + ((OrderItem) obj).getOrderItemId());
        return this.item.getItemId() == ((OrderItem) obj).getItem().getItemId();
    }

}
