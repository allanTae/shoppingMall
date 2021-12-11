package com.allan.shoppingMall.domains.delivery.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderCancelFailException;
import com.allan.shoppingMall.common.value.Address;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @Column(name="delivery_memo", nullable = false)
    private String deliveryMemo;

    @Builder
    public Delivery(DeliveryStatus deliveryStatus, Address address, String deliveryMemo) {
        this.deliveryStatus = deliveryStatus;
        this.address = address;
        this.deliveryMemo = deliveryMemo;
    }

    /**
     * 배송 취소 메소드.
     */
    public void cancelDelivery(){
        if(deliveryStatus == DeliveryStatus.DELIVERY_READY)
            this.deliveryStatus = DeliveryStatus.DELIVERY_CANCEL;
        else{
            throw new OrderCancelFailException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED.getMessage(), ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
        }
    }
}
