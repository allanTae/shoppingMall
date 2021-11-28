package com.allan.shoppingMall.common.exception;

public class OrderFailException extends BusinessException{

    public OrderFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public OrderFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
