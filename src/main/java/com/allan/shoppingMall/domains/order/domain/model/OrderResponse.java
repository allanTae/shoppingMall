package com.allan.shoppingMall.domains.order.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * order domain api 응답 object
 * 결제 프로세스 전 주문 도메인을 생성하는 api 에 반환 내용을 담고 있습니다.
 */
@Getter
@Setter
public class OrderResponse {
    private String orderResult; // 결과 메시지.
    private String orderNum; // 주문 번호, 주문 도메인의 주문번호 조회 api 의 결과 값으로 사용하는 필드로, 조회시에만 사용합니다(조회 외 api 시 사용시에는 "empty" 값을 반환하도록 합시다.)

    private OrderErrorResponse errorResponse;

    public OrderResponse(String orderResult, String orderNum) {
        this.orderResult = orderResult;
        this.orderNum = orderNum;
    }

    public OrderResponse(String orderResult, String orderNum, OrderErrorResponse errorResponse) {
        this.orderResult = orderResult;
        this.orderNum = orderNum;
        this.errorResponse = errorResponse;
    }
}
