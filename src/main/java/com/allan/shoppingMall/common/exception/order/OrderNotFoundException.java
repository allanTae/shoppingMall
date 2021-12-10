package com.allan.shoppingMall.common.exception.order;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public OrderNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
