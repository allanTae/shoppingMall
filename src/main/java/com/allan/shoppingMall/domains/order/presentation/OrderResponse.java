package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.domains.order.presentation.OrderErrorResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * server order api response
 * order rest api 응답 클래스입니다.
 */
@Getter
@Setter
public class OrderResponse {
    private String apiResultMessage; // 결과 메시지.
    private Boolean apiResult; // api 결과 값. 사용자단에서 결과를 확인하기 위해 사용합니다.

    private String orderNum; // 주문 번호, 주문 도메인의 주문번호 조회 api 의 결과 값으로 사용하는 필드로, 조회시에만 사용합니다(조회 외 api 시 사용시에는 "empty" 값을 반환하도록 합시다.)

    private OrderErrorResponse errorResponse;

    public OrderResponse(OrderResult orderResult, String orderNum) {
        this.apiResult = orderResult.getResult();
        this.apiResultMessage = orderResult.getMessage();
        this.orderNum = orderNum;
    }

    public OrderResponse(OrderResult orderResult, String orderNum, OrderErrorResponse errorResponse) {
        this.apiResult = orderResult.getResult();
        this.apiResultMessage = orderResult.getMessage();
        this.orderNum = orderNum;
        this.errorResponse = errorResponse;
    }
}
