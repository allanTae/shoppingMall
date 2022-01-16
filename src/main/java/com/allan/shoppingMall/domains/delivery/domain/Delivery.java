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

    @Column(nullable = false)
    private String recipient;

    @Column(name="recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name="delivery_memo", nullable = false)
    private String deliveryMemo;

    @Column(name = "delivery_amount") // not null 필요.
    private Long deliveryAmount;

    @Builder
    public Delivery(DeliveryStatus deliveryStatus, Address address, String deliveryMemo, String recipient, String recipientPhone) {
        this.deliveryStatus = deliveryStatus;
        setAddressAndAmount(address);
        this.deliveryMemo = deliveryMemo;
        this.recipient = recipient;
        this.recipientPhone =recipientPhone;
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

    /**
     * 주소 오브젝트와 배송비를 설정하는 메소드.
     * @param address 주소 정보.
     */
    private void setAddressAndAmount(Address address){
        // 주소 설정.
        this.address = address;
        // 배송비 설정.
        boolean isJeju = address.getPostCode().substring(0, 2).equals("63");
        if(isJeju){
            this.deliveryAmount = 4000l;
        }else{
            this.deliveryAmount = 3000l;
        }
    }
}
