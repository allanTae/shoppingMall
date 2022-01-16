package com.allan.shoppingMall.domains.payment.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 주문페이지(orderForm.jsp)에서 결제에 대한 유효성 검사 api 를 호출 할 때 전달하는 결제정보를 담고 있는 object.
 */
@Getter
@Setter
public class PaymentRequest {
    private String imp_uid;
    private String merchant_uid;
}
