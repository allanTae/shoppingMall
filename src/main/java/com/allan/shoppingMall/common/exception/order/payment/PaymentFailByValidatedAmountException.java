package com.allan.shoppingMall.common.exception.order.payment;

import com.allan.shoppingMall.common.exception.ErrorCode;

public class PaymentFailByValidatedAmountException extends PaymentFailException{
    public PaymentFailByValidatedAmountException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PaymentFailByValidatedAmountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
