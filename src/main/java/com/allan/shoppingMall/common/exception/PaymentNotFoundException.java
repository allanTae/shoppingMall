package com.allan.shoppingMall.common.exception;

public class PaymentNotFoundException extends BusinessException{

    public PaymentNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PaymentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
