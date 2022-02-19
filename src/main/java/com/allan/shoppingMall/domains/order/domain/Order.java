package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderCancelFailException;
import com.allan.shoppingMall.common.exception.order.payment.PaymentFailByValidatedOrderStatusException;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.payment.domain.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 정보를 저장히기 위한 엔티티.
 * 주문 상태는 다음과 같은 순서로 진행 됩니다.
 * 임시주문 => 상품준비중 => 주문접수 => 주문완료(자세한 상태 정보는 OrderStatus 참조)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Slf4j
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member orderer;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Embedded
    private OrdererInfo ordererInfo;

    @Column(name = "order_num", unique = true)
    private String orderNum;

    @Column(name="payment_num", unique = true)
    private String paymentNum;

    // 주문 생성시, OrderStatus 기본 설정.
    // 주문상태의 기본값은 '임시주문' 상태입니다.
    // 주문 정보는 order domain id + 날자 정보로 이루어집니다.
    @PrePersist
    public void setUp(){
        this.orderStatus = OrderStatus.ORDER_TEMP;
    }

    // orderId 를 사용해서 주문번호를 생성함.
    // orderId 는 현재 Identity strategy 를 사용하고 있기 때문에 데이터베이스 저장시점에 생성이 된다.
    // 그렇기에, 저장된 이후 실행되는 endUp() 를 통해서 저장하도록 한다.
    @PostPersist
    public void endUp(){
        String dateInfo = this.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String orderNum = "order_uid_" + this.orderId + "_" + dateInfo;
        this.orderNum = orderNum;
        log.info(this.orderId + " orderNum: " + orderNum);
    }

    @Builder
    public Order(Member orderer, Delivery delivery, OrdererInfo ordererInfo, String orderNum) {
        this.orderer = orderer;
        this.delivery = delivery;
        this.ordererInfo = ordererInfo;
        this.orderNum = orderNum;
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
        // 주문상태 점검.
        if(this.orderStatus == OrderStatus.ORDER_COMPLETE || this.orderStatus == OrderStatus.ORDER_ITEM_READY || this.orderStatus == OrderStatus.ORDER_TEMP){
            this.orderStatus = OrderStatus.ORDER_CANCEL;
            // 배송 취소.
            this.delivery.cancelDelivery();
            for(OrderItem orderItem : this.orderItems){
                if( orderItem instanceof OrderClothes){
                    OrderClothes orderClothes = (OrderClothes) orderItem;
                    orderClothes.cancleOrderClothes();
                }else{
                    orderItem.cancelOrderItem();
                }
            }
        }else{
            throw new OrderCancelFailException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
        }
    }

    /**
     * 주문을 결제하는 메소드.
     * 현재 주문의 상태를 결제완료(상품준비중) 상태로 변경한다.(주문의 상태 정보에 대해선 OrderStatus 참조하세요.)
     */
    public void payOrder(String impUid) {
       if(this.orderStatus == OrderStatus.ORDER_TEMP){
           this.paymentNum = impUid;
           this.orderStatus = OrderStatus.ORDER_COMPLETE;
       }else{
           throw new PaymentFailByValidatedOrderStatusException(ErrorCode.PAYMENT_INVALID_ORDER_STATUS);
       }
    }

}
