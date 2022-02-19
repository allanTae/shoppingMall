package com.allan.shoppingMall.domains.order.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order api 결과를 나타내는 enum 클래스 입니다.
 */
@AllArgsConstructor
@Getter
public enum OrderResult {
    ORDER_SUCCESS(true,"주문에 성공하였습니다."),
    ORDER_FAIL(false,"주문에 실패하였습니다."),
    PAYMENT_SUCCESS(true, "결제에 성공하였습니다."),
    PAYMENT_FAIL(false, "결제에 실패하였습니다."),
    REFUND_SUCCESS(true, "환불에 성공하였습니다."),
    REFUND_FAIL(false, "환불에 실패하였습니다."),
    PAYMENT_REFUND_FAIL(false, "결제 및 환불에 실패하였습니다.");

    private Boolean result;
    private String message;
}
