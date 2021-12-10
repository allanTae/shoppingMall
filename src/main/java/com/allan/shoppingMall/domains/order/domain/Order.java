package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderCancelFailException;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 정보를 저장히기 위한 엔티티.
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Slf4j
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OrderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member orderer;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Embedded
    private OrdererInfo ordererInfo;

    @Builder
    public Order(Member orderer, OrderStatus orderStatus, Delivery delivery, OrdererInfo ordererInfo) {
        this.orderer = orderer;
        this.orderStatus = orderStatus;
        this.delivery = delivery;
        this.ordererInfo = ordererInfo;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * Order Entity 측에서 OrderItem 추가하도록 비즈니스 로직을 처리함.
     *
     * 동일한 OrderItem 이 추가되는 것을 방지(동일한 상품의 이중결제 방지).
     * @param
     */
    public void changeOrderItems(List<OrderItem> orderItems){
        for(OrderItem orderItem : orderItems){
            if(this.orderItems.contains(orderItem)){
                log.error("order entity changeOrderItems() cause error!");
                log.error("orderItem is already existed in orderItems list");
            }else{
                this.orderItems.add(orderItem);
                orderItem.changeOrder(this);
            }
        }
    }

    /**
     * 고객이 주문 취소 할때 사용하는 메소드.
     * orderItem 의 구현체에 따라 다른 메소드를 호출하도록 처리.
     */
    public void cancelOrder(){
        log.info("cancelOrder() call!!!");
        // 주문상태 점검.
        if(this.orderStatus == OrderStatus.ORDER_ITEM_READY){
            this.orderStatus = OrderStatus.ORDER_CANCEL;
            // 배송 취소.
            this.delivery.cancelDelivery();
            for(OrderItem orderItem : this.orderItems){
                if( orderItem instanceof OrderClothes){
                    log.info("orderItem is OrderClothes!!");
                    OrderClothes orderClothes = (OrderClothes) orderItem;
                    orderClothes.cancleOrderClothes();
                }else{
                    log.info("orderItem is not OrderClothes");
                    orderItem.cancelOrderItem();
                }

            }
        }else{
            throw new OrderCancelFailException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED.getMessage(), ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
        }
    }
}
