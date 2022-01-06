package com.allan.shoppingMall.common.exception.order.payment;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class PaymentFailException extends BusinessException {

    public PaymentFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PaymentFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
