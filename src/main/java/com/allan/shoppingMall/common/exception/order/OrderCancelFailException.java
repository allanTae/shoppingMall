package com.allan.shoppingMall.common.exception.order;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class OrderCancelFailException extends BusinessException {

    public OrderCancelFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public OrderCancelFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
