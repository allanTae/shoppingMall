package com.allan.shoppingMall.common.exception.cart;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class CartNotFoundException extends BusinessException {

    public CartNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CartNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
