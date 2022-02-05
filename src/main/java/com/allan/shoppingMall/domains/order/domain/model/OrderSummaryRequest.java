package com.allan.shoppingMall.domains.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 의상 상세보기 창에서 주문상세 창으로 정보를 전달하기 위해 서버측으로 전달 할 데이터 입니다.
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class OrderSummaryRequest {
    private Long totalQuantity;
    private Long totalAmount;
    private List<OrderItemSummaryRequest> orderItems;
    private Long deliveryAmount = 3000l; // 기본 배송료.

    public OrderSummaryRequest(Long totalAmount, Long totalQuantity, List<OrderItemSummaryRequest> orderItems){
        setTotalAmount(totalAmount);
        this.totalQuantity = totalQuantity;
        this.orderItems = orderItems;
    }

    public void setTotalAmount(Long totalAmount){
        this.totalAmount = totalAmount;
        if(totalAmount > 50000)
            this.deliveryAmount = 0l;
    }
}
