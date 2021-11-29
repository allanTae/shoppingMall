package com.allan.shoppingMall.common.exception.order;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class OrderFailException extends BusinessException {

    public OrderFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public OrderFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
