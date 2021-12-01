package com.allan.shoppingMall.domains.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문 상태를 나타냄.
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER_COMPLETE("주문완료"),
    ORDER_CANCEL("주문취소"),
    ORDER_READY("주문접수"),
    ORDER_ITEM_READY("상품준비중");

    private String desc;
}
