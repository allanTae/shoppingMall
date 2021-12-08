package com.allan.shoppingMall.domains.order.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 의상 상세보기 창에서 주문상세 창으로 정보를 전달하기 위해 서버측으로 전달 할 데이터 입니다.
 */
@Getter
@Setter
public class OrderSummaryRequest {
    private Long itemId;
    private Long totalQuantity;
    private Long totalAmount;
    private List<OrderItemSummary> orderItems;
}
