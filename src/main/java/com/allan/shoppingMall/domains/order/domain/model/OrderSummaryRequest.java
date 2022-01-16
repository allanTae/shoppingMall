package com.allan.shoppingMall.domains.order.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 의상 상세보기 창에서 주문상세 창으로 정보를 전달하기 위해 서버측으로 전달 할 데이터 입니다.
 */
@Getter
@Setter
@Slf4j
public class OrderSummaryRequest {
    private Long totalQuantity;
    private Long totalAmount;
    private List<OrderItemSummaryRequest> orderItems;
    private Boolean isDeliveryFree; // 배송비 무료.
    private Long deliveryAmount = 3000l; // 기본 배송료.
    private Long additionalAmount = 1500l; // 추가 배송료.

    public void setTotalAmount(Long totalAmount){
        this.totalAmount = totalAmount;
        log.info("setTotalAmount() call!!!");
        log.info("totalAmount: " + this.totalAmount);
        if(totalAmount > 50000)
            this.isDeliveryFree = true;
        else
            this.isDeliveryFree = false;

        log.info("isDeliveryFree: " + this.isDeliveryFree);
    }
}
