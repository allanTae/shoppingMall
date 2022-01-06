package com.allan.shoppingMall.common.exception.order.payment;

import com.allan.shoppingMall.common.exception.ErrorCode;

public class PaymentFailByValidatedOrderStatusException extends PaymentFailException{
    public PaymentFailByValidatedOrderStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PaymentFailByValidatedOrderStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
