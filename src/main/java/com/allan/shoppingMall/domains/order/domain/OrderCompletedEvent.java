package com.allan.shoppingMall.domains.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문 완료 이벤트 클래스 입니다.
 */
@Getter
@AllArgsConstructor
public class OrderCompletedEvent {
    private Long orderId;
    private String authId;
}
